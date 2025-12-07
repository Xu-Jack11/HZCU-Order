import styles from './page.module.css';
import { Users, Store, TrendingUp, AlertCircle } from 'lucide-react';

export default function AdminDashboardPage() {
    return (
        <div>
            <h2 style={{ fontSize: '1.5rem', fontWeight: 600, marginBottom: '1.5rem', color: 'var(--foreground)' }}>平台概览</h2>

            <div className={styles.grid}>
                <div className={styles.card}>
                    <div className={styles.cardHeader}>
                        <div className={styles.cardLabel}>注册用户</div>
                        <Users size={18} className={styles.icon} style={{ color: '#6366f1' }} />
                    </div>
                    <div className={styles.cardValue}>12,580</div>
                    <div className={styles.cardTrend} style={{ color: 'var(--success)' }}>+12.5% 较上周</div>
                </div>

                <div className={styles.card}>
                    <div className={styles.cardHeader}>
                        <div className={styles.cardLabel}>入驻食堂</div>
                        <Store size={18} className={styles.icon} style={{ color: '#ec4899' }} />
                    </div>
                    <div className={styles.cardValue}>8</div>
                    <div className={styles.cardTrend}>运营正常</div>
                </div>

                <div className={styles.card}>
                    <div className={styles.cardHeader}>
                        <div className={styles.cardLabel}>今日GMV</div>
                        <TrendingUp size={18} className={styles.icon} style={{ color: '#10b981' }} />
                    </div>
                    <div className={styles.cardValue}>¥45,230</div>
                    <div className={styles.cardTrend} style={{ color: 'var(--success)' }}>+5.2% 较昨日</div>
                </div>

                <div className={styles.card}>
                    <div className={styles.cardHeader}>
                        <div className={styles.cardLabel}>系统告警</div>
                        <AlertCircle size={18} className={styles.icon} style={{ color: '#f59e0b' }} />
                    </div>
                    <div className={styles.cardValue}>0</div>
                    <div className={styles.cardTrend}>系统运行健康</div>
                </div>
            </div>

            <div style={{ marginTop: '2rem', padding: '2rem', background: 'var(--card)', borderRadius: '1rem', border: '1px solid var(--border)' }}>
                <h3 style={{ marginBottom: '1rem', fontSize: '1.1rem' }}>平台趋势</h3>
                <div style={{ height: '300px', display: 'flex', alignItems: 'center', justifyContent: 'center', background: 'var(--muted)', borderRadius: '0.5rem', color: 'var(--muted-foreground)' }}>
                    图表组件待集成 (Recharts/Chart.js)
                </div>
            </div>
        </div>
    );
}
