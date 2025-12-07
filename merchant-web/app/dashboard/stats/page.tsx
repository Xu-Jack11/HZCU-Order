import styles from './page.module.css';

export default function StatsPage() {
    return (
        <div>
            <h2 style={{ fontSize: '1.5rem', fontWeight: 600, marginBottom: '1.5rem' }}>数据概览</h2>

            <div className={styles.grid}>
                <div className={styles.card}>
                    <div className={styles.cardLabel}>今日订单</div>
                    <div className={styles.cardValue}>128</div>
                </div>
                <div className={styles.card}>
                    <div className={styles.cardLabel}>今日营业额</div>
                    <div className={styles.cardValue}>¥3,240.00</div>
                </div>
                <div className={styles.card}>
                    <div className={styles.cardLabel}>累计用户</div>
                    <div className={styles.cardValue}>1,024</div>
                </div>
                <div className={styles.card}>
                    <div className={styles.cardLabel}>平均评分</div>
                    <div className={styles.cardValue}>4.8</div>
                </div>
            </div>

            <div className={styles.chartContainer}>
                {/* Placeholder for a chart library like Recharts or Chart.js */}
                <div style={{ textAlign: 'center' }}>
                    <p>近7日销售趋势图</p>
                    <p style={{ fontSize: '0.8rem', marginTop: '1rem' }}>(图表组件待集成)</p>
                </div>
            </div>
        </div>
    );
}
