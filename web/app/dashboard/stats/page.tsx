"use client";
import { useEffect, useState } from 'react';
import styles from './page.module.css';
import { api } from '@/lib/api';

export default function StatsPage() {
    const [loading, setLoading] = useState<boolean>(false);
    const [error, setError] = useState<string | null>(null);
    const [todayCount, setTodayCount] = useState<number>(0);
    const [todayRevenue, setTodayRevenue] = useState<number>(0);
    const [userCount, setUserCount] = useState<number>(0);

    useEffect(() => {
        const load = async () => {
            setLoading(true);
            setError(null);
            try {
                // 拉取全部已完成订单（历史累计），计算数量与金额
                const page = await api.getOrders({ status: 'COMPLETED', page: 1, pageSize: 10000 });
                const orders = page.list || [];
                const count = orders.length;
                const revenue = orders.reduce((sum, o) => {
                    const amount = (o as any).totalAmount ?? (o as any).totalPrice ?? 0;
                    // 若后端不提供总价，则用明细求和作为兜底
                    const fallback = Array.isArray((o as any).items)
                        ? ((o as any).items as any[]).reduce((s, it) => {
                            const price = (it as any).price ?? 0;
                            const qty = (it as any).quantity ?? 1;
                            return s + price * qty;
                        }, 0)
                        : 0;
                    return sum + (Number(amount) || fallback);
                }, 0);
                setTodayCount(count);
                setTodayRevenue(revenue);
                setUserCount(count);
            } catch (e: any) {
                setError(e.message || '加载统计失败');
            } finally {
                setLoading(false);
            }
        };
        load();
    }, []);

    return (
        <div>
            <h2 style={{ fontSize: '1.5rem', fontWeight: 600, marginBottom: '1.5rem' }}>数据概览</h2>

            <div className={styles.grid}>
                <div className={styles.card}>
                    <div className={styles.cardLabel}>今日订单</div>
                    <div className={styles.cardValue}>{loading ? '…' : todayCount}</div>
                </div>
                <div className={styles.card}>
                    <div className={styles.cardLabel}>今日营业额</div>
                    <div className={styles.cardValue}>{loading ? '…' : `¥${todayRevenue.toFixed(2)}`}</div>
                </div>
                <div className={styles.card}>
                    <div className={styles.cardLabel}>累计用户</div>
                    <div className={styles.cardValue}>{loading ? '…' : userCount}</div>
                </div>
                <div className={styles.card}>
                    <div className={styles.cardLabel}>平均评分</div>
                    <div className={styles.cardValue}>4.8</div>
                </div>
            </div>

            {error && <div style={{ color: '#c00', marginTop: '1rem' }}>错误：{error}</div>}

            <div className={styles.chartContainer}>
                <div style={{ textAlign: 'center' }}>
                    <p>近7日销售趋势图</p>
                    <p style={{ fontSize: '0.8rem', marginTop: '1rem' }}>(图表组件待集成)</p>
                </div>
            </div>
        </div>
    );
}
