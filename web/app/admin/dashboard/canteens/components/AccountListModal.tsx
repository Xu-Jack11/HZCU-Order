'use client';

import { useState, useEffect } from 'react';
import { X, Key, Loader2, ShieldCheck } from 'lucide-react';
import styles from '../page.module.css';
import { api } from '@/lib/api';

interface Account {
    merchantAccountId: number;
    username: string;
    realName: string;
    mobile: string;
    role: string;
    status: number;
}

interface AccountListModalProps {
    canteenId: string;
    canteenName: string;
    onClose: () => void;
}

export default function AccountListModal({ canteenId, canteenName, onClose }: AccountListModalProps) {
    const [accounts, setAccounts] = useState<Account[]>([]);
    const [loading, setLoading] = useState(true);
    const [resettingId, setResettingId] = useState<number | null>(null);

    const fetchAccounts = async () => {
        setLoading(true);
        try {
            const res = await api.admin.getMerchantAccounts(canteenId);
            if (res.success) {
                setAccounts(res.data);
            }
        } catch (err) {
            console.error('Failed to fetch accounts', err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchAccounts();
    }, [canteenId]);

    const handleResetPassword = async (account: Account) => {
        const newPass = prompt(`请输入 "${account.username}" 的新密码:`);
        if (!newPass) return;

        setResettingId(account.merchantAccountId);
        try {
            const res = await api.admin.resetMerchantPassword(account.merchantAccountId.toString(), newPass);
            if (res.success) {
                alert('密码重置成功');
            }
        } catch (err: any) {
            alert(err.message || '操作失败');
        } finally {
            setResettingId(null);
        }
    };

    return (
        <div className={styles.modalOverlay} onClick={(e) => e.target === e.currentTarget && onClose()}>
            <div className={styles.modal} style={{ width: '600px' }}>
                <div className={styles.modalHeader}>
                    <h3 className={styles.modalTitle}>{canteenName} - 账号管理</h3>
                    <button className={styles.closeBtn} onClick={onClose}>
                        <X size={20} />
                    </button>
                </div>
                <div className={styles.modalContent}>
                    {loading ? (
                        <div style={{ textAlign: 'center', padding: '2rem' }}>
                            <Loader2 size={24} className={styles.spin} style={{ margin: '0 auto' }} />
                            <p style={{ marginTop: '0.5rem', color: '#64748b' }}>加载中...</p>
                        </div>
                    ) : (
                        <div className={styles.tableWrapper} style={{ border: '1px solid #e2e8f0', borderRadius: '0.5rem' }}>
                            <table className={styles.table} style={{ margin: 0 }}>
                                <thead>
                                    <tr>
                                        <th>用户名</th>
                                        <th>真实姓名</th>
                                        <th>角色</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {accounts.map(acc => (
                                        <tr key={acc.merchantAccountId}>
                                            <td style={{ fontWeight: 500 }}>{acc.username}</td>
                                            <td>{acc.realName}</td>
                                            <td>
                                                <span style={{ fontSize: '0.75rem', background: '#f1f5f9', padding: '0.2rem 0.5rem', borderRadius: '1rem', color: '#475569' }}>
                                                    {acc.role === 'ADMIN' ? '超级管理员' : '操作员'}
                                                </span>
                                            </td>
                                            <td>
                                                <button
                                                    className={styles.actionBtn}
                                                    onClick={() => handleResetPassword(acc)}
                                                    disabled={resettingId === acc.merchantAccountId}
                                                    style={{ color: '#ef4444' }}
                                                >
                                                    {resettingId === acc.merchantAccountId ?
                                                        <Loader2 size={14} className={styles.spin} /> :
                                                        <Key size={14} />
                                                    } 重置密码
                                                </button>
                                            </td>
                                        </tr>
                                    ))}
                                    {accounts.length === 0 && (
                                        <tr>
                                            <td colSpan={4} style={{ textAlign: 'center', padding: '2rem', color: '#64748b' }}>未找到账号</td>
                                        </tr>
                                    )}
                                </tbody>
                            </table>
                        </div>
                    )}
                    <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: '1.5rem' }}>
                        <button onClick={onClose} className={styles.secondaryBtn}>
                            关闭
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
