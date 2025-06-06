package com.team01.project.domain.user.controller

import MembershipDto
import com.team01.project.domain.user.service.MembershipService
import com.team01.project.global.dto.RsData
import com.team01.project.global.exception.MembershipErrorCode
import com.team01.project.global.exception.MembershipException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Membership", description = "멤버십 API")
@RestController
@RequestMapping("/membership")
class MembershipController(
    private val membershipService: MembershipService
) {
    @GetMapping("/my")
    @Operation(
        summary = "현재 로그인한 사용자의 멤버십 조회",
        description = "사용자의 멤버십 등급, 기간, 자동 갱신 여부 반환"
    )
    fun getMyMembership(
        @AuthenticationPrincipal user: OAuth2User
    ): RsData<MembershipDto> {
        val userId = user.getAttribute<String>("id")
            ?: throw MembershipException(MembershipErrorCode.UNAUTHORIZED)

        val membership = membershipService.getCurrentUserMembership(userId)
        return RsData("200-1", "요금제 정보 조회 성공", membership)
    }

    @PostMapping("/cancel")
    @Operation(
        summary = "요금제 해지",
        description = "현재 사용 중인 프리미엄 요금제를 해지합니다."
    )
    fun cancelMembership(
        @AuthenticationPrincipal user: OAuth2User
    ): RsData<String> {
        val userId = user.getAttribute<String>("id")
            ?: throw MembershipException(MembershipErrorCode.UNAUTHORIZED)

        membershipService.cancelMembership(userId)
        return RsData("200-2", "요금제가 정상적으로 해지되었습니다.", null)
    }

    @PostMapping("/init")
    @Operation(
        summary = "기본 멤버십 생성",
        description = "멤버십이 존재하지 않을 경우 basic 등급으로 생성합니다."
    )
    fun initMembership(
        @AuthenticationPrincipal user: OAuth2User
    ): RsData<String> {
        val userId = user.getAttribute<String>("id")
            ?: throw MembershipException(MembershipErrorCode.UNAUTHORIZED)

        val created = membershipService.initMembership(userId)

        return if (created) {
            RsData("200-4", "기본 멤버십이 생성되었습니다.", null)
        } else {
            RsData("200-5", "이미 멤버십이 존재합니다.", null)
        }
    }

    @GetMapping("/admin")
    @Operation(
        summary = "[관리자 전용] 모든 사용자 멤버십 조회",
        description = "전체 유저의 멤버십 정보 리스트를 반환합니다."
    )
    fun getAllMemberships(): RsData<List<Map<String, Any>>> {
        val memberships = membershipService.getAllMemberships()
        return RsData("200-6", "전체 멤버십 정보 조회 성공", memberships)
    }

    @PatchMapping("/admin/{id}")
    @Operation(summary = "멤버십 정보 수정", description = "관리자가 멤버십 정보를 수정합니다.")
    fun updateMembership(
        @PathVariable id: String,
        @RequestBody request: MembershipDto
    ): RsData<String> {
        membershipService.updateMembership(id, request)
        return RsData("200-7", "멤버십 정보가 수정되었습니다.", null)
    }
}
