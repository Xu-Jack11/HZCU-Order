'use client';

import { Download, TrendingUp, TrendingDown, DollarSign } from 'lucide-react';
import styles from './page.module.css';

export default function FinancePage() {
    return (
        <div>
            <div className={styles.header}>
                <h2 className={styles.title}>财务报表</h2>
                <div className={styles.controls}>
                    <input type="date" className={styles.datePicker} />
                    <button className={styles.exportBtn}>
                        <Download size={18} /> 导出报表
                    </button>
                </div>
            </div>

            <div className={styles.statsGrid}>
                <div className={styles.card}>
                    <div className={styles.cardLabel}>本月总营收</div>
                    <div className={styles.cardValue}>¥128,450.00</div>
                    <div className={styles.cardTrend}>
                        <TrendingUp size={16} />
                        <span>+12.5% 较上月</span>
                    </div>
                </div>
                <div className={styles.card}>
                    <div className={styles.cardLabel}>平台净利润 (抽成)</div>
                    <div className={styles.cardValue}>¥6,422.50</div>
                    <div className={styles.cardTrend}>
                        <TrendingUp size={16} />
                        <span>+8.2% 较上月</span>
                    </div>
                </div>
                <div className={styles.card}>
                    <div className={styles.cardLabel}>待结算金额</div>
                    <div className={styles.cardValue}>¥45,230.00</div>
                    <div className={styles.cardTrend} style={{ color: 'var(--muted-foreground)' }}>
                        <span>3个账期进行中</span>
                    </div>
                </div>
            </div>

            <div className={styles.section}>
                <h3 className={styles.sectionTitle}>近期交易记录</h3>
                <table className={styles.table}>
                    <thead>
                        <tr>
                            <th>交易单号</th>
                            <th>时间</th>
                            <th>类型</th>
                            <th>说明</th>
                            <th>金额</th>
                            <th>状态</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>TRX_20251208001</td>
                            <td>2025-12-08 10:23:45</td>
                            <td>订单支付</td>
                            <td>用户支付 - 杭州城院第一食堂</td>
                            <td className={`${styles.amount} ${styles.income}`}>+¥28.50</td>
                            <td>支付成功</td>
                        </tr>
                        <tr>
                            <td>TRX_20251208002</td>
                            <td>2025-12-08 10:25:12</td>
                            <td>订单支付</td>
                            <td>用户支付 - 清真餐厅</td>
                            <td className={`${styles.amount} ${styles.income}`}>+¥15.00</td>
                            <td>支付成功</td>
                        </tr>
                        <tr>
                            <td>SET_20251208001</td>
                            <td>2025-12-08 09:00:00</td>
                            <td>商家结算</td>
                            <td>结算转账 - 杭州城院第二食堂</td>
                            <td className={`${styles.amount} ${styles.expense}`}>-¥4,250.00</td>
                            <td>已打款</td>
                        </tr>
                        <tr>
                            <td>REF_20251208005</td>
                            <td>2025-12-08 08:30:22</td>
                            <td>订单退款</td>
                            <td>用户取消 - 订单 #10086</td>
                            <td className={`${styles.amount} ${styles.expense}`}>-¥35.00</td>
                            <td>退款成功</td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <div className={styles.section}>
                <h3 className={styles.sectionTitle}>营收趋势分析</h3>
                <div style={{ height: '300px', display: 'flex', alignItems: 'center', justifyContent: 'center', background: 'var(--muted)', borderRadius: '0.5rem', color: 'var(--muted-foreground)' }}>
                    Chart Component Placeholder (Revenue/Profit Trends)
                </div>
            </div>
        </div>
    );
}
