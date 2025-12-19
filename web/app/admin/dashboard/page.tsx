'use client';

import { useState, useEffect } from 'react';
import styles from './page.module.css';
import { Users, Store, TrendingUp, AlertCircle } from 'lucide-react';
import { api } from '@/lib/api';

export default function AdminDashboardPage() {
    const [stats, setStats] = useState<any>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchStats = async () => {
            try {
                const res = await api.stats.getPlatformStats();
                if (res.success) {
                    setStats(res.data);
                }
            } catch (err) {
                console.error('Failed to fetch platform stats', err);
            } finally {
                setLoading(false);
            }
        };
        fetchStats();
    }, []);

    if (loading) {
        return <div style={{ padding: '2rem', textAlign: 'center' }}>加载中...</div>;
    }

    return (
        <div>
            <h2 style={{ fontSize: '1.5rem', fontWeight: 600, marginBottom: '1.5rem', color: 'var(--foreground)' }}>平台概览</h2>

            <div className={styles.grid}>
                <div className={styles.card}>
                    <div className={styles.cardHeader}>
                        <div className={styles.cardLabel}>注册用户</div>
                        <Users size={18} className={styles.icon} style={{ color: '#6366f1' }} />
                    </div>
                    <div className={styles.cardValue}>{stats?.totalUsers || 0}</div>
                    <div className={styles.cardTrend}>总注册人数</div>
                </div>

                <div className={styles.card}>
                    <div className={styles.cardHeader}>
                        <div className={styles.cardLabel}>运营中食堂</div>
                        <Store size={18} className={styles.icon} style={{ color: '#ec4899' }} />
                    </div>
                    <div className={styles.cardValue}>{stats?.activeCanteens || 0}</div>
                    <div className={styles.cardTrend}>目前提供服务</div>
                </div>

                <div className={styles.card}>
                    <div className={styles.cardHeader}>
                        <div className={styles.cardLabel}>今日成交额</div>
                        <TrendingUp size={18} className={styles.icon} style={{ color: '#10b981' }} />
                    </div>
                    <div className={styles.cardValue}>¥{stats?.todayRevenue?.toFixed(2) || '0.00'}</div>
                    <div className={styles.cardTrend} style={{ color: (stats?.revenueGrowth || 0) >= 0 ? 'var(--success)' : 'var(--error)' }}>
                        {(stats?.revenueGrowth || 0) >= 0 ? '+' : ''}{stats?.revenueGrowth?.toFixed(1) || 0}% 较昨日
                    </div>
                </div>

                <div className={styles.card}>
                    <div className={styles.cardHeader}>
                        <div className={styles.cardLabel}>昨日成交额</div>
                        <AlertCircle size={18} className={styles.icon} style={{ color: '#f59e0b' }} />
                    </div>
                    <div className={styles.cardValue}>¥{stats?.yesterdayRevenue?.toFixed(2) || '0.00'}</div>
                    <div className={styles.cardTrend}>昨日全站总额</div>
                </div>
            </div>

            <div style={{ marginTop: '2rem', padding: '2rem', background: 'var(--card)', borderRadius: '1rem', border: '1px solid var(--border)' }}>
                <h3 style={{ marginBottom: '1rem', fontSize: '1.1rem' }}>平台运行状态</h3>
                <div style={{ padding: '3rem', textAlign: 'center', background: 'var(--muted)', borderRadius: '0.5rem', color: 'var(--muted-foreground)' }}>
                    全系统监控：正常运行中
                </div>
            </div>
        </div>
    );
}
