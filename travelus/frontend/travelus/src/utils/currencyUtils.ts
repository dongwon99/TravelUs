export const formatCurrency = (value: number, currencyCode: string): string => {
  const formatter = new Intl.NumberFormat('ko-kr', {
    style: 'currency',
    currency: currencyCode,
    minimumFractionDigits: currencyCode === 'KRW' ? 0 : 2,
    maximumFractionDigits: currencyCode === 'KRW' ? 0 : 2,
  });
  return formatter.format(value);
};

export const getLatestRate = (rates: { [date: string]: number }): number => {
  const dates = Object.keys(rates);
  const latestDate = dates[dates.length - 1];
  return rates[latestDate];
}

// 추가적인 유틸리티 함수들
export const calculateChange = (oldValue: number, newValue: number): number => {
  return newValue - oldValue;
}

export const calculatePercentageChange = (oldValue: number, newValue: number): number => {
  return ((newValue - oldValue) / oldValue) * 100;
}

export const roundToDecimalPlaces = (value: number, places: number): number => {
  const factor = Math.pow(10, places);
  return Math.round(value * factor) / factor;
}

export const calculateDailyChange = (todayRate: number, yesterdayRate: number): number => {
  return todayRate - yesterdayRate;
}

export const formatExchangeRate = (rate: number, currencyCode: string): string => {
  let formattedRate: number;
  
  if (currencyCode === 'JPY') {
    formattedRate = rate * 100;
    return formattedRate.toFixed(2);
  } else if (currencyCode === 'KRW') {
    formattedRate = rate;
    return formattedRate.toFixed(0);
  } else {
    formattedRate = rate;
    return formattedRate.toFixed(2);
  }
}