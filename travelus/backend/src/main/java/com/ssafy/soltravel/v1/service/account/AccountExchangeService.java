package com.ssafy.soltravel.v1.service.account;

import com.ssafy.soltravel.v1.domain.redis.PreferenceRate;
import com.ssafy.soltravel.v1.dto.exchange.Account;
import com.ssafy.soltravel.v1.dto.exchange.ExchangeRateRegisterRequestDto;
import com.ssafy.soltravel.v1.repository.redis.PreferenceRateRepository;
import java.util.HashSet;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountExchangeService {

    private final PreferenceRateRepository preferenceRateRepository;

    /**
     * 환전 선호 금액 설정
     */
    public void setPreferenceRate(String accountNo, ExchangeRateRegisterRequestDto dto) {

        String id = makeId(dto.getCurrencyCode(), dto.getExchangeRate());
        Optional<PreferenceRate> exchangeOpt = preferenceRateRepository.findById(id);

        PreferenceRate preference;
        if (exchangeOpt.isPresent()) {
            preference = exchangeOpt.get();
        } else {
            preference = new PreferenceRate(id, new HashSet<>());
        }

        preference.getAccounts().add(new Account(dto.getGeneralAccountId(), accountNo));
        preferenceRateRepository.save(preference);
    }

    public String makeId(String currency, double rate) {
        return String.format("%s(%.2f)", currency, rate);
    }

}
