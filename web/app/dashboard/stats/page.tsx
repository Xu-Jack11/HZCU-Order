'use client';

import { useState, useEffect } from 'react';
import { api } from '@/lib/api';
import styles from './page.module.css';

export default function StatsPage() {
    const [stats, setStats] = useState<any>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchStats = async () => {
            try {
                const res = await api.stats.getMerchantStats(7);
                if (res.success) {
                    setStats(res.data);
                }
            } catch (err) {
                console.error('Failed to fetch stats', err);
            } finally {
                setLoading(false);
            }
        };
        fetchStats();
    }, []);

    if (loading) return <div className={styles.loading}>加载中...</div>;

    return (
        <div>
            <h2 style={{ fontSize: '1.5rem', fontWeight: 600, marginBottom: '1.5rem' }}>数据概览</h2>

            <div className={styles.grid}>
                <div className={styles.card}>
                    <div className={styles.cardLabel}>今日订单</div>
                    <div className={styles.cardValue}>{stats?.todayOrderCount || 0}</div>
                </div>
                <div className={styles.card}>
                    <div className={styles.cardLabel}>今日营业额</div>
                    <div className={styles.cardValue}>¥{stats?.todayRevenue?.toFixed(2) || '0.00'}</div>
                </div>
                <div className={styles.card}>
                    <div className={styles.cardLabel}>平均评分</div>
                    <div className={styles.cardValue}>{stats?.averageRating || '---'}</div>
                </div>
            </div>

            <div className={styles.chartContainer}>
                <div style={{ textAlign: 'center' }}>
                    <p>热销榜单 (Top 5)</p>
                    <div style={{ marginTop: '1.5rem', textAlign: 'left', maxWidth: '400px', margin: '1.5rem auto' }}>
                        {stats?.topDishes?.length > 0 ? (
                            stats.topDishes.map((dish: any, idx: number) => (
                                <div key={idx} style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.8rem', padding: '0.5rem', background: '#f9fafb', borderRadius: '4px' }}>
                                    <span>{idx + 1}. {dish.dishName}</span>
                                    <span style={{ fontWeight: 600, color: '#4f46e5' }}>{dish.quantity} 份</span>
                                </div>
                            ))
                        ) : (
                            <p style={{ color: '#999', fontSize: '0.9rem' }}>暂无数据</p>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}
