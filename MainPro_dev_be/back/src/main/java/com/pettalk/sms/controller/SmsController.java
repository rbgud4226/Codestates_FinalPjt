package com.pettalk.sms.controller;

import com.pettalk.member.repository.MemberRepository;
import com.pettalk.sms.service.RedisService;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@RestController
public class SmsController {

    final DefaultMessageService messageService;
    private MemberRepository memberRepository;
    private final RedisService redisService;

    public SmsController( MemberRepository memberRepository,
                          RedisService redisService,
                          @Value("${coolsms.api.key}")String apiKey,
                          @Value("${coolsms.api.secret}")String apiSecret) {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
        this.memberRepository =memberRepository;
        this.redisService = redisService;
    }


    @PostMapping("/sendSms")
    public SingleMessageSentResponse sendMessage(@RequestBody Map<String, String> request) {
        String to = request.get("phone");
        String authCode = randomAuthCode();
        redisService.setPhoneNumberWithExpiration(to, authCode, 5, TimeUnit.MINUTES);
        Message message = new Message();
        message.setFrom("01012345678");
        message.setTo(to);
        message.setText("인증 코드: " + authCode);
        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);
        return response;
    }

    @PostMapping("/registration")
    public ResponseEntity smsRegistration(@RequestParam String authCode, @RequestParam String phone) {
        String storedAuthCode = redisService.getPhoneNumber(phone);
        if (storedAuthCode == null) {
            return new ResponseEntity<>("인증 코드가 만료되었습니다", HttpStatus.BAD_REQUEST);
        }
        if (authCode.equals(storedAuthCode)) {
            redisService.deletePhoneNumber(phone);
            return new ResponseEntity<>("핸드폰 인증 완료", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("인증 코드가 일치하지 않습니다", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/check")
    public ResponseEntity checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        boolean isExist = memberRepository.existsByEmail(email);
        if (isExist) {
            return new ResponseEntity<>("이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("사용 가능한 이메일입니다.", HttpStatus.OK);
        }
    }

    @PostMapping("/check/nickname")
    public ResponseEntity checkNickName(@RequestBody Map<String, String> request) {
        String nickname = request.get("nickname");
        boolean isExist = memberRepository.existsByNickName(nickname);
        if (isExist) {
            return new ResponseEntity<>("이미 존재하는 닉네임입니다.", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("사용 가능한 닉네임입니다.", HttpStatus.OK);
        }
    }

    private String randomAuthCode() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }
}




