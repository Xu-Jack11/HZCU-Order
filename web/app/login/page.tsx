'use client';

import { useRouter } from 'next/navigation';
import { useState } from 'react';
import styles from './page.module.css';
import { api } from '@/lib/api';

export default function LoginPage() {
    const router = useRouter();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [account, setAccount] = useState('');
    const [password, setPassword] = useState('');
    const [loginType, setLoginType] = useState<'merchant' | 'admin'>('merchant');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const res = loginType === 'merchant'
                ? await api.auth.loginMerchant({ username: account, password })
                : await api.auth.loginAdmin({ username: account, password });

            if (res.success) {
                localStorage.setItem('token', res.data.token);
                localStorage.setItem('user', JSON.stringify(res.data.user));
                localStorage.setItem('role', loginType === 'merchant' ? 'ROLE_MERCHANT' : 'ROLE_ADMIN');

                // Redirect to appropriate dashboard
                const targetPath = loginType === 'merchant' ? '/dashboard' : '/admin/dashboard';
                router.push(targetPath);
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
                <h1 className={styles.title}>{loginType === 'merchant' ? '商家登录' : '系统管理登录'}</h1>
                <p className={styles.subtitle}>智能食堂点餐平台管理端</p>

                <div className={styles.typeSelector}>
                    <button
                        className={loginType === 'merchant' ? styles.activeType : ''}
                        onClick={() => setLoginType('merchant')}
                    >
                        商家
                    </button>
                    <button
                        className={loginType === 'admin' ? styles.activeType : ''}
                        onClick={() => setLoginType('admin')}
                    >
                        管理员
                    </button>
                </div>

                {error && <div className={styles.error}>{error}</div>}

                <form className={styles.form} onSubmit={handleSubmit}>
                    <div className={styles.inputGroup}>
                        <label className={styles.label} htmlFor="account">账号</label>
                        <input
                            id="account"
                            className={styles.input}
                            type="text"
                            placeholder="请输入账号"
                            value={account}
                            onChange={(e) => setAccount(e.target.value)}
                            required
                        />
                    </div>

                    <div className={styles.inputGroup}>
                        <label className={styles.label} htmlFor="password">密码</label>
                        <input
                            id="password"
                            className={styles.input}
                            type="password"
                            placeholder="请输入密码"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>

                    <button className={styles.button} type="submit" disabled={loading}>
                        {loading ? '登录中...' : '登 录'}
                    </button>
                </form>
            </div>
        </div>
    );
}
