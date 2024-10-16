package com.ssafy.soltravel.v1.controller;

import com.ssafy.soltravel.v1.dto.transaction.TransactionHistoryDto;
import com.ssafy.soltravel.v1.dto.transaction.request.ForeignTransactionRequestDto;
import com.ssafy.soltravel.v1.dto.transaction.request.TransactionHistoryRequestDto;
import com.ssafy.soltravel.v1.dto.transaction.request.TransactionRequestDto;
import com.ssafy.soltravel.v1.dto.transaction.request.TransferRequestDto;
import com.ssafy.soltravel.v1.dto.transaction.response.DepositResponseDto;
import com.ssafy.soltravel.v1.dto.transaction.response.TransferHistoryResponseDto;
import com.ssafy.soltravel.v1.service.transaction.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Transaction API", description = "거래 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "계좌 입금", description = "지정된 계좌에 입금합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "입금 성공", content = @Content(schema = @Schema(implementation = DepositResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/{accountNo}/deposit")
    public ResponseEntity<DepositResponseDto> postAccountDeposit(
        @PathVariable String accountNo,
        @RequestBody TransactionRequestDto requestDto
    ) {
        ResponseEntity<DepositResponseDto> response = transactionService.postAccountDeposit(accountNo, requestDto);

        return response;
    }

    @Operation(summary = "계좌 출금", description = "지정된 계좌에서 출금합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "출금 성공", content = @Content(schema = @Schema(implementation = DepositResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/{accountNo}/withdraw")
    public ResponseEntity<DepositResponseDto> postAccountWithdrawal(
        @PathVariable String accountNo,
        @RequestBody TransactionRequestDto requestDto
    ) {

        ResponseEntity<DepositResponseDto> response = transactionService.postAccountWithdrawal(accountNo, requestDto);

        return response;
    }

    @Operation(summary = "계좌 이체", description = "지정된 계좌에서 다른 계좌로 이체합니다.")
    @ApiResponses(value = {

        @ApiResponse(responseCode = "200", description = "이체 성공",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransferHistoryResponseDto.class)))
        ),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @PostMapping("/{accountNo}/transfer")
    public ResponseEntity<List<TransferHistoryResponseDto>> postAccountTransfer(
        @PathVariable String accountNo,
        @RequestBody TransferRequestDto requestDto
    ) {
        ResponseEntity<List<TransferHistoryResponseDto>> response =
            transactionService.postAccountTransfer(accountNo, requestDto);

        return response;
    }


    // 거래 내역 조회
    @Operation(summary = "거래 내역 조회", description = "지정된 계좌의 거래 내역을 조회합니다. 거래 유형 (M:입금, D:출금, A:전체)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = TransactionHistoryDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    @GetMapping("/{accountNo}/history")
    public ResponseEntity<List<TransactionHistoryDto>> getHistoryByAccountNo(
        @PathVariable String accountNo,
        @ModelAttribute TransactionHistoryRequestDto requestDto
    ) {

        ResponseEntity<List<TransactionHistoryDto>> response = transactionService.getHistoryByAccountNo(accountNo,
            requestDto);

        return response;
    }

    /**
     * 외화 계좌 입금
     */
    @PostMapping("/foreign/{accountNo}/deposit")
    @Operation(summary = "외화 계좌 입금", description = "외화 계좌에 입금합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "입금 성공", content = @Content(schema = @Schema(implementation = DepositResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<DepositResponseDto> postForeignAccountDeposit(
        @Parameter(description = "사용자의 계좌 번호", example = "0887850232491646")
        @PathVariable String accountNo,
        @RequestBody ForeignTransactionRequestDto requestDto
    ) {
        return ResponseEntity.ok().body(transactionService.postForeignDeposit(accountNo, requestDto));
    }

    @PostMapping("/foreign/{accountNo}/withdrawal")
    @Operation(summary = "외화 계좌 출금", description = "외화 계좌에서 출금합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "출금 성공", content = @Content(schema = @Schema(implementation = DepositResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<DepositResponseDto> postForeignAccountWithdrawal(
        @Parameter(description = "사용자의 계좌 번호", example = "0887850232491646")
        @PathVariable String accountNo,
        @RequestBody ForeignTransactionRequestDto requestDto
    ) {
        return ResponseEntity.ok().body(transactionService.postForeignWithdrawal(false,accountNo, requestDto));
    }


    /**
     * 외화 계좌 거래 내역 조회 getForeignHistoryByAccountNo
     */
    @GetMapping("/foreign/{accountNo}/history")
    @Operation(summary = "외화 계좌 거래 내역 조회", description = "외화 계좌의 거래 내역을 조회합니다. transactionType(M:입금, D:출금, A:전체)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "거래 내역 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionHistoryDto.class)))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content)
    })
    public ResponseEntity<List<TransactionHistoryDto>> getForeignHistoryByAccountNo(
        @Parameter(description = "사용자의 계좌 번호", example = "0887850232491646")
        @PathVariable String accountNo,
        @ModelAttribute TransactionHistoryRequestDto requestDto
    ) {

        return ResponseEntity.ok().body(transactionService.getForeignHistoryByAccountNo(accountNo, requestDto));
    }
}
