import { ChartOptions, ChartDataset, Plugin } from 'chart.js';

declare module 'chart.js' {
  interface PluginOptionsByType<TType> {
    minMaxLabels?: {
      afterDatasetDraw: (chart: Chart) => void;
    }
  }
}