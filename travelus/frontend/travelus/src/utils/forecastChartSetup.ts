import { ChartOptions } from 'chart.js';
import 'chartjs-adapter-date-fns';
import { ko } from 'date-fns/locale';

export const forecastChartSetup = (
  currencyCode: string, 
  formatExchangeRate: (value: number, currencyCode: string) => string, 
  trend: string
): ChartOptions<"line"> => {
  const trendColor = trend === 'UPWARD' ? 'rgb(221, 82, 87)' : trend === 'DOWNWARD' ? 'rgb(72, 128, 238)' : 'rgb(128, 128, 128)';

  return {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: false,
      },
      tooltip: {
        enabled: true,
        mode: 'index',
        intersect: false,
        callbacks: {
          label: (context) => {
            let label = context.dataset.label || '';
            if (label) {
              label += ': ';
            }
            if (context.parsed.y !== null) {
              label += formatExchangeRate(context.parsed.y, currencyCode);
            }
            return label;
          },
        },
      },
    },
    scales: {
      x: {
        type: 'time',
        time: {
          unit: 'month',
          displayFormats: {
            month: 'M월'
          },
        },
        adapters: {
          date: {
            locale: ko,
          },
        },
        ticks: {
          autoSkip: true,
          maxTicksLimit: 6,
        },
        grid: {
          display: false,
        },
        border: {
          display: false,
        }
      },
      y: {
        display: false,
        grid: {
          display: false,
        },
        border: {
          display: false,
        },
      },
    },
    elements: {
      line: {
        borderColor: trendColor,
        tension: 0.1,
      },
      point: {
        radius: 0,
        hoverRadius: 7,
        backgroundColor: trendColor,
      },
    },
    interaction: {
      intersect: false,
      mode: 'index',
    },
  };
};