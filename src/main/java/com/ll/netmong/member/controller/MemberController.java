package com.ll.netmong.member.controller;

import com.ll.netmong.common.RsData;
import com.ll.netmong.jwt.TokenDto;
import com.ll.netmong.member.dto.JoinRequest;
import com.ll.netmong.member.dto.LoginDto;
import com.ll.netmong.member.dto.UsernameRequest;
import com.ll.netmong.member.entity.Member;
import com.ll.netmong.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/find")
    public Member findMember() {
        return memberService.findById(1L);
    }

    @PostMapping("/join")
    public RsData<Member> join(@Valid @RequestBody JoinRequest joinRequest) {

        //create
        Member user = memberService.createMember(joinRequest);

        return RsData.successOf(user);
    }

    @PostMapping("/dup-username")
    public RsData checkDupUsername(@Valid @RequestBody UsernameRequest usernameRequest) {

        if (memberService.isDuplicateUsername(usernameRequest)) {
            return RsData.of("F-1", "이미 중복된 아이디가 있습니다.");
        }

        return RsData.of("S-1", "사용가능한 아이디입니다.");
    }

    @PostMapping("/login")
    public RsData<TokenDto> login(@Valid @RequestBody LoginDto loginDto) throws Exception {

        TokenDto tokenDto = memberService.login(loginDto);

        return RsData.successOf(tokenDto);
    }
}
