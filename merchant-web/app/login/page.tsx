'use client';

import { useRouter } from 'next/navigation';
import { useState } from 'react';
import styles from './page.module.css';

export default function LoginPage() {
    const router = useRouter();
    const [loading, setLoading] = useState(false);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        // Mock login delay
        setTimeout(() => {
            router.push('/dashboard');
        }, 800);
    };

    return (
        <div className={styles.container}>
            <div className={styles.card}>
                <h1 className={styles.title}>商家登录</h1>
                <p className={styles.subtitle}>智能食堂点餐平台商家管理端</p>

                <form className={styles.form} onSubmit={handleSubmit}>
                    <div className={styles.inputGroup}>
                        <label className={styles.label} htmlFor="account">账号</label>
                        <input
                            id="account"
                            className={styles.input}
                            type="text"
                            placeholder="请输入商家账号"
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
