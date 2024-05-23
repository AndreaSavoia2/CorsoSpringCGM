package it.cgmconsulting.myblog.service;


import it.cgmconsulting.myblog.entity.*;
import it.cgmconsulting.myblog.entity.enumeration.AuthorityName;
import it.cgmconsulting.myblog.entity.enumeration.Frequency;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.mail.Mail;
import it.cgmconsulting.myblog.mail.MailService;
import it.cgmconsulting.myblog.payload.request.ChangeMeRequest;
import it.cgmconsulting.myblog.payload.request.ChangePasswordRequest;
import it.cgmconsulting.myblog.payload.request.SigninRequest;
import it.cgmconsulting.myblog.payload.request.SignupRequest;
import it.cgmconsulting.myblog.payload.response.AuthenticationResponse;
import it.cgmconsulting.myblog.payload.response.AvatarResponse;
import it.cgmconsulting.myblog.payload.response.GetMeResponse;
import it.cgmconsulting.myblog.repository.AuthorityRepository;
import it.cgmconsulting.myblog.repository.AvatarRepository;
import it.cgmconsulting.myblog.repository.UserRepository;
import it.cgmconsulting.myblog.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    @Value("${application.confirmCode.validity}")
    private long validity;

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailService mailService;
    private final RegistrationService registrationService;
    private final AvatarRepository avatarRepository;
    private final ConsentService consentService;

    @Transactional
    public String signup(SignupRequest request) {
        if(!request.isAcceptRules()){
            return "Without consent is not possibile to proceed";
        }

        if(userRepository.existsByUsernameOrEmail(request.getUsername(), request.getEmail())){
            return null;
        }
        //creazione utente
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()));

        Authority authority = authorityRepository.findByAuthorityDefaultTrue();
        user.setAuthorities(Collections.singleton(authority));
        userRepository.save(user);

        Consent consent = null;
        if(request.isSendNewsLetter()){
            consent = new Consent(new ConsentId(user), true, true, Frequency.WEEKLY,null);
        }else {
            consent = new Consent(new ConsentId(user), true, false, Frequency.NEVER,null);
        }
        consentService.save(consent);

        //creazione token di validità
        Registration registration = Registration.builder()
                .confirmCode(UUID.randomUUID().toString())
                .endDate(LocalDateTime.now().plusMinutes(validity))
                .user(user)
                .build();
        registrationService.save(registration);

        Mail mail = mailService.createMail(user,
                "MyBlog - Registration confirm",
                "Hi " + user.getUsername() + ",\n" +
                        "please click here to confirm your email \n" +
                        "http://localhost:8090/api/v0/auth/?confirmCode="+registration.getConfirmCode());

        mailService.sendMail(mail);

        return "User successfully registered. Please check your email to confirm the registration.";
    }

    @Transactional
    public AuthenticationResponse signin(SigninRequest request){

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new BadCredentialsException("Bad credentials");
        }

        boolean isGuest = isGuest(authorities(user.getAuthorities()));

        if(!user.isEnabled() && isGuest){
            throw new DisabledException("You didn't confirm your email still");
        }

        if(!user.isEnabled() && !isGuest){
            if (user.getBannedUntil().isAfter(LocalDateTime.now())){
                user.setEnabled(true);
                user.setBannedUntil(null);
            }else {
                throw new DisabledException("You are banned until " + user.getBannedUntil().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
        }

        String jwt = jwtService.generateToken(user, user.getId());

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(authorities(user.getAuthorities()))
                .token(jwt)
                .build();

        return authenticationResponse;
    }

    private String[] authorities(Collection<? extends GrantedAuthority> auths){
        return auths.stream().map(GrantedAuthority::getAuthority)
                .toArray(String[]::new);
    }

    private boolean isGuest(String[] authorities){
        return Arrays.stream(authorities).anyMatch(
                s -> s.contains(authorityRepository.findByAuthorityDefaultTrue()
                        .getAuthorityName()
                        .name()));
    }

    @Transactional
    public String confirm(String confirmCode){
        // Verificare che il token (confirmCode) non sia scaduto
        // se è valido. Abilitare lo user, cambiare authority
        // altrimenti re inviare l'email con nuovo token
        Optional<Registration> reg = registrationService.findByConfirmCode(confirmCode);

        if(reg.isPresent() && LocalDateTime.now().isAfter(reg.get().getEndDate())){
            Registration oldRegistration = reg.get();
            Registration newRegistration = Registration.builder()
                    .confirmCode(UUID.randomUUID().toString())
                    .endDate(LocalDateTime.now().plusMinutes(validity))
                    .user(oldRegistration.getUser())
                    .build();
            registrationService.save(newRegistration);
            registrationService.delete(oldRegistration);

            Mail mail = mailService.createMail(newRegistration.getUser(),
                    "MyBlog - Registration confirm",
                    "Hi " + newRegistration.getUser().getUsername() + ",\n" +
                            "please click here to confirm your email \n" +
                            "http://localhost:8090/api/v0/auth/?confirmCode="+newRegistration.getConfirmCode());
            mailService.sendMail(mail);

            return "A new confirmation code has been sent to you. Please check your email";
        }
        else if (reg.isEmpty()){
            return "You already confirmed your registration";
        }
        else {

            reg.get().getUser().setEnabled(true);
            Authority authority = authorityRepository.findByAuthorityName(AuthorityName.MEMBER);
            reg.get().getUser().setAuthorities(Collections.singleton(authority));
            registrationService.delete(reg.get());
            return "Now you are enable. Please log in";
        }

    }

    @Transactional
    public AuthenticationResponse changeRole(int id, Set<String> auths, UserDetails userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if(userDetails.getUsername().equals(user.getUsername())){
            return null;
        }

        Set<AuthorityName> authorityNames = EnumSet.noneOf(AuthorityName.class);
        for(String s : auths){
            authorityNames.add(AuthorityName.valueOf(s));
        }
        Set<Authority> authorities = authorityRepository.findByAuthorityNameIn(authorityNames);
        user.setAuthorities(authorities);

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .authorities(authorities(user.getAuthorities()))
                .build();

        return authenticationResponse;

    }

    public String changePassword(ChangePasswordRequest request, UserDetails userDetails){

        User user = (User) userDetails;

        if (!request.newPwd().equals(request.newPwdConfirm())){
            return "New Password and new Password Confirm are different";
        }

        if (!passwordEncoder.matches(request.oldPwd(), user.getPassword())){
            return "Wrong old Password";
        }

        user.setPassword(passwordEncoder.encode(request.newPwd()));
        userRepository.save(user);
        return "New password had been set. Please re-login";

    }

    @Transactional
    public String resetPassword(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("User", "username", username));
        String pwd = RandomStringUtils.randomAlphanumeric(10);

        Mail mail = mailService.createMail(user,
                "MyBlog - Reset password",
                "Hi " + user.getUsername() + ",\n use this password " + pwd + " to login");
        mailService.sendMail(mail);

        user.setPassword(passwordEncoder.encode(pwd));
        return "You password reset. check email";
    }

    public GetMeResponse getMe(UserDetails userDetails){

        User user = (User) userDetails;
        Avatar avatar = avatarRepository.findById( new AvatarId(user)).orElse(null);
        AvatarResponse avatarResponse = null;

        if (avatar != null){
            avatarResponse = new AvatarResponse(
                    avatar.getAvatarId().getUserId().getId(),
                    avatar.getFilename(),
                    avatar.getFiletype(),
                    avatar.getData());
        }

        GetMeResponse me = GetMeResponse.builder()
                .id(user.getId())
                .lastname(user.getLatname())
                .firstname(user.getFirstname())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .avatar(avatarResponse)
                .build();

        return me;
    }

    public GetMeResponse changeMe(ChangeMeRequest request, UserDetails userDetails){

        User user = (User) userDetails;

        if(userRepository.existsByEmailAndIdNot(request.email(), user.getId())){
            return null;
        }

        GetMeResponse me = GetMeResponse.builder()
                .lastname(request.lastname())
                .firstname(request.firstname())
                .email(request.email())
                .bio(request.bio())
                .build();

        user.setLatname(me.getLastname());
        user.setFirstname(me.getFirstname());
        user.setEmail(me.getEmail());
        user.setBio(me.getBio());
        userRepository.save(user);

        return me;
    }

    public String deleteMe(UserDetails userDetails){
        User user = (User) userDetails;
        user.setEnabled(false);
        String s = UUID.randomUUID().toString();
        user.setEmail(s);
        user.setFirstname(s);
        user.setLatname(s);
        user.setBio(s);
        user.setUsername(s.substring(0,19));
        userRepository.save(user);
        return "Your account has been removed";
    }

    public String updateConsent(UserDetails userDetails, Frequency frequency){

        User user = (User) userDetails;
        Consent consent = consentService.findByConsentId(new ConsentId(user));

        consent.setSendNewsLetter(!frequency.equals(Frequency.NEVER));
        consent.setFrequency(frequency);

        consentService.save(consent);

        return "Updated consents";
    }
}
