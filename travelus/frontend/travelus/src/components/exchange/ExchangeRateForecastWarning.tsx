import React from "react";
import { Drawer, Typography, Box, List, ListItem, ListItemText } from "@mui/material";

interface RecommendationDrawerProps {
  open: boolean;
  onClose: () => void;
}

const RecommendationDrawer: React.FC<RecommendationDrawerProps> = ({ open, onClose }) => {
  const predictionChallenges = [
    "시장의 변동성: 환율은 다양한 경제, 정치적 요인에 의해 급변할 수 있어요",
    "글로벌 이벤트: 예측하기 어려운 세계적 사건들이 환율에 큰 영향을 미칠 수 있어요",
    "정책 변화: 중앙은행의 정책 변화가 환율에 즉각적인 영향을 줄 수 있어요",
    "데이터의 한계: 과거 데이터가 항상 미래를 정확히 예측하기는 어려워요",
  ];

  const usedInformation = [
    "과거 환율 데이터",
    "경제 지표 (GDP, 인플레이션율 등)",
    "중앙은행 정책 동향",
    "국제 무역 데이터",
    "정치적 안정성 지수",
  ];

  return (
    <Drawer anchor="bottom" open={open} onClose={onClose}>
      <Box sx={{ p: 3, maxHeight: "80vh", overflowY: "auto" }}>
        <Typography variant="h5" gutterBottom>
          환율 예측에 대한 이해
        </Typography>

        <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
          환율 예측의 어려움
        </Typography>
        <List>
          {predictionChallenges.map((challenge, index) => (
            <ListItem key={index}>
              <ListItemText primary={challenge} />
            </ListItem>
          ))}
        </List>

        <Typography variant="h6" gutterBottom sx={{ mt: 2 }}>
          환율 예측에 사용된 정보
        </Typography>
        <List>
          {usedInformation.map((info, index) => (
            <ListItem key={index}>
              <ListItemText primary={info} />
            </ListItem>
          ))}
        </List>

        <Typography variant="body2" sx={{ mt: 2, fontStyle: "italic" }}>
          주의: 환율 예측은 참고용으로만 사용하시기 바랍니다. 실제 환율은 예측과 다를 수 있으며, 금융 결정을 내릴 때는
          항상 다양한 요소를 고려해야 합니다.
        </Typography>
      </Box>
    </Drawer>
  );
};

export default RecommendationDrawer;
