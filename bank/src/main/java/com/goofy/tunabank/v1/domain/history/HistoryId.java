package com.goofy.tunabank.v1.domain.history;

import com.goofy.tunabank.v1.domain.Enum.TransactionType;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HistoryId {

    private Long id;
    private TransactionType transactionType;

    @Override
    public int hashCode() {
        return Objects.hash(id, transactionType);
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }
        HistoryId transactionHistoryId = (HistoryId) obj;
        return Objects.equals(id, transactionHistoryId.id) && transactionType == transactionHistoryId.transactionType;
    }
}
