'use client';

import { useRouter } from 'next/navigation';
import { useState } from 'react';
import { ShieldCheck } from 'lucide-react';
import styles from './page.module.css';
import { api } from '@/lib/api';

export default function AdminLoginPage() {
    const router = useRouter();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [formData, setFormData] = useState({ account: '', password: '' });

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const res = await api.auth.loginAdmin({
                username: formData.account,
                password: formData.password
            });

            if (res.success) {
                localStorage.setItem('token', res.data.token);
                localStorage.setItem('user', JSON.stringify(res.data.user));
                localStorage.setItem('role', 'ROLE_ADMIN');
                router.push('/admin/dashboard');
            } else {
                setError(res.message || '登录失败');
            }
        } catch (err: any) {
            setError(err.message || '网络请求失败');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.card}>
                <div style={{ textAlign: 'center' }}>
                    <div className={styles.adminBadge}>
                        System Admin
                    </div>
                </div>
                <h1 className={styles.title}>系统管理员登录</h1>
                <p className={styles.subtitle}>HZCU-Order 平台管理后台</p>

                {error && <div className={styles.error}>{error}</div>}

                <form className={styles.form} onSubmit={handleSubmit}>
                    <div className={styles.inputGroup}>
                        <label className={styles.label} htmlFor="account">管理员账号</label>
                        <input
                            id="account"
                            className={styles.input}
                            type="text"
                            placeholder="请输入管理员账号"
                            value={formData.account}
                            onChange={(e) => setFormData({ ...formData, account: e.target.value })}
                            required
                        />
                    </div>

                    <div className={styles.inputGroup}>
                        <label className={styles.label} htmlFor="password">密码</label>
                        <input
                            id="password"
                            className={styles.input}
                            type="password"
                            placeholder="••••••••"
                            value={formData.password}
                            onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                            required
                        />
                    </div>

                    <button className={styles.button} type="submit" disabled={loading}>
                        {loading ? '登录中...' : '登 录'}
                    </button>

                    <div style={{ textAlign: 'center', marginTop: '1rem' }}>
                        <span style={{ fontSize: '0.8rem', color: '#64748b' }}>
                            <ShieldCheck size={14} style={{ display: 'inline', marginRight: '4px', verticalAlign: 'text-bottom' }} />
                            安全登录已启用
                        </span>
                    </div>
                </form>
            </div>
        </div>
    );
}
