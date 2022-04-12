package com.project.springemailclient.controllers;

import com.project.springemailclient.utils.Constant;
import com.project.springemailclient.utils.FormRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;

@Controller
public class SendMailControlller {

    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping("/send")
    public String demoSendMail() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(Constant.MAIL_TO);
        simpleMailMessage.setSubject("This is message");
        simpleMailMessage.setText("This is content mail");

        //Send mail
        javaMailSender.send(simpleMailMessage);

        return "Send mail successfully";
    }


    @GetMapping("/send-html")
    public String sendMailHTML() throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        String htmlMsg = "<h3 style='color:red;'>Content width html</h3>"
                +"<img style='width:300px' src='https://res.cloudinary.com/det1eswrj/image/upload/v1649671875/profile_tzwrie.jpg'>";

        message.setContent(htmlMsg, "text/html");
        helper.setSubject("This is subject");
        helper.setTo(Constant.MAIL_TO);

        javaMailSender.send(message);

        return "send mail successfully";
    }

    //send mail from form html
    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("formRequest",new FormRequest());
        return "index";
    }

    @ResponseBody
    @PostMapping("/send-form")
    public String sendMailForm(@ModelAttribute("formRequest") FormRequest formRequest) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setSubject(formRequest.getSubject());
        helper.setTo(formRequest.getMailTo());
        helper.setText(formRequest.getContent());

        for(MultipartFile file : formRequest.getFiles()){
            helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
        }

        javaMailSender.send(message);

        return "send mail successfully";
    }

}
