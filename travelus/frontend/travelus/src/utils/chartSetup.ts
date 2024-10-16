import { ChartOptions } from 'chart.js';
import 'chartjs-adapter-date-fns';
import { ko } from 'date-fns/locale';

export const setupChart = (currencyCode: string, formatExchangeRate: (value: number, currencyCode: string) => string, isIncreasing: boolean): ChartOptions<"line"> => {
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
        },
        adapters: {
          date: {
            locale: ko,
          },
        },
        ticks: {
          display: false,
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
        borderColor: isIncreasing ? 'rgb(221, 82, 87)' : 'rgb(72, 128, 238)',
        tension: 0.1,
      },
      point: {
        radius: 0,
        hoverRadius: 7,
        backgroundColor: isIncreasing ? 'rgb(221, 82, 87)' : 'rgb(72, 128, 238)',
      },
    },
    interaction: {
      intersect: false,
      mode: 'index',
    },
  };
};