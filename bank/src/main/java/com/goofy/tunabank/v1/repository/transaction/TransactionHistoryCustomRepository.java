package com.goofy.tunabank.v1.repository.transaction;

import com.goofy.tunabank.v1.domain.history.AbstractHistory;
import com.goofy.tunabank.v1.dto.transaction.request.TransactionHistoryListRequestDto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionHistoryCustomRepository {

    //    Optional<List<AbstractHistory>> findByCustomOrder(TransactionHistoryListRequestDto requestDto);
    Optional<List<AbstractHistory>> findHistoryByAccountNo(TransactionHistoryListRequestDto requestDto);
    Optional<Page<AbstractHistory>> findHistoryByAccountNo(
        TransactionHistoryListRequestDto requestDto, Pageable pageable);
}
