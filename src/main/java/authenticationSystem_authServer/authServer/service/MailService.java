package authenticationSystem_authServer.authServer.service;

import authenticationSystem_authServer.authServer.domain.MailAuth;
import authenticationSystem_authServer.authServer.repository.MailAuthRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private String authNum;
    private MailAuthRepository mailAuthRepository;

    public MailService(JavaMailSender javaMailSender, SpringTemplateEngine springTemplateEngine, MailAuthRepository mailAuthRepository) {
        this.javaMailSender = javaMailSender;
        this.springTemplateEngine = springTemplateEngine;
        this.mailAuthRepository = mailAuthRepository;
    }

    public void createCode(){
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i=0;i<8;i++){
            int index = random.nextInt(3);
            switch (index) {
                case 0 -> key.append((char) ((int) random.nextInt(26) + 97));
                case 1 -> key.append((char) ((int) random.nextInt(26) + 65));
                case 2 -> key.append(random.nextInt(9));
            }
        }
        authNum = key.toString();
    }

    public MimeMessage createEmailForm(String email,String titleStr) throws MessagingException, UnsupportedEncodingException{
        createCode();
        String setFrom = "kim0208yh@naver.com";
        String title = setTitle(titleStr);

        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject(title);
        message.setFrom(setFrom);
        if(Objects.equals(titleStr, "signup")){
            message.setText(setContext(authNum),"utf-8","html");
        }else{
            message.setText(setPasswordMailContext(authNum),"utf-8","html");
        }

        return message;
    }
    public String setTitle(String title){
        if(Objects.equals(title, "signup")){
           return "AuthSystem 회원가입 인증번호";
        }else if(Objects.equals(title,"passwordAuth")){
            return "AuthSystem 임시비밀번호";
        }
        return null;
    }
    public String sendEmail(String toEmail, String title) throws MessagingException, UnsupportedEncodingException{
        MimeMessage emailForm = createEmailForm(toEmail,title);
        javaMailSender.send(emailForm);

        return authNum;
    }

    public String setContext(String code){
        Context context = new Context();
        context.setVariable("code",code);
        return springTemplateEngine.process("mail",context);
    }

    public String setPasswordMailContext(String code){
        Context context = new Context();
        context.setVariable("code",code);
        return springTemplateEngine.process("passwordMail",context);
    }

    public void saveCode(MailAuth mailAuth){
        mailAuthRepository.save(mailAuth);
    }
    public MailAuth findMailAuthByUserId(String userId){
        return mailAuthRepository.findByMemberId(userId).orElse(null);
    }

    public void deleteMailAuth(String memberId){
        mailAuthRepository.deleteByMemberId(memberId);
    }
}
