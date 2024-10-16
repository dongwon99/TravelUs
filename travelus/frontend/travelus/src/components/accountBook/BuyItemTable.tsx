import React from "react";
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material";
import Paper from "@mui/material/Paper";
import { BuyItemInfo } from "../../types/accountBook";

interface Props {
  buyItems: BuyItemInfo[];
}

const BuyItemTable = ({ buyItems }: Props) => {
  return (
    <TableContainer sx={{ maxHeight: 200 }} component={Paper}>
      <Table sx={{ width: "100%" }} stickyHeader aria-label="sticky table">
        <TableHead>
          <TableRow>
            <TableCell>상품명</TableCell>
            <TableCell align="center">금액</TableCell>
            <TableCell align="center">수량</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {buyItems.length === 0 ? (
            <TableCell>구매 상품이 없습니다.</TableCell>
          ) : (
            <>
              {buyItems.map((item, index) => (
                <TableRow key={index} sx={{ "&:last-child td, &:last-child th": { border: 0 } }}>
                  <TableCell component="th" scope="row">
                    {item.item}
                  </TableCell>
                  <TableCell align="center">{item.price}</TableCell>
                  <TableCell align="center">{item.quantity}</TableCell>
                </TableRow>
              ))}
            </>
          )}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default BuyItemTable;
