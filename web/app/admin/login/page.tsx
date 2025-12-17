'use client';

import { useRouter } from 'next/navigation';
import { useState } from 'react';
import { ShieldCheck } from 'lucide-react';
import styles from './page.module.css';

export default function AdminLoginPage() {
    const router = useRouter();
    const [loading, setLoading] = useState(false);
    const [formData, setFormData] = useState({ account: '', password: '' });

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        // Implement admin login logic here
        // For prototype, we'll just redirect to dashboard
        console.log('Admin Login attempt:', formData);

        setTimeout(() => {
            router.push('/admin/dashboard');
        }, 800);
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

                <form className={styles.form} onSubmit={handleSubmit}>
                    <div className={styles.inputGroup}>
                        <label className={styles.label} htmlFor="account">管理员账号</label>
                        <input
                            id="account"
                            className={styles.input}
                            type="text"
                            placeholder="Please enter admin account"
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
                        <span style={{ fontSize: '0.8rem', color: 'var(--muted-foreground)' }}>
                            <ShieldCheck size={14} style={{ display: 'inline', marginRight: '4px', verticalAlign: 'text-bottom' }} />
                            Security Connection
                        </span>
                    </div>
                </form>
            </div>
        </div>
    );
}
