'use client';

import { useState, useEffect } from 'react';
import { Search, Ban, CheckCircle, Edit2, X } from 'lucide-react';
import styles from './page.module.css';
import { api } from '@/lib/api';

interface User {
    id: string;
    nickname: string;
    avatarUrl: string;
    status: number; // 0: Banned, 1: Normal
    lastLoginAt: string;
}

export default function UsersPage() {
    const [users, setUsers] = useState<User[]>([]);
    const [loading, setLoading] = useState(true);
    const [searchTerm, setSearchTerm] = useState('');

    const fetchUsers = async () => {
        setLoading(true);
        try {
            const res = await api.admin.getUsers();
            if (res.success) {
                const mappedUsers = res.data.map((u: any) => ({
                    id: u.userId.toString(),
                    nickname: u.nickname || 'Unknown',
                    avatarUrl: u.avatarUrl,
                    status: u.status,
                    lastLoginAt: u.lastLoginAt ? new Date(u.lastLoginAt).toLocaleDateString() : 'Never'
                }));
                setUsers(mappedUsers);
            }
        } catch (err) {
            console.error('Failed to fetch users', err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    const toggleStatus = async (id: string, currentStatus: number) => {
        const newStatus = currentStatus === 1 ? 0 : 1;
        try {
            const res = await api.admin.updateUserStatus(id, newStatus);
            if (res.success) {
                fetchUsers();
            }
        } catch (err) {
            alert('操作失败');
        }
    };

    const filteredUsers = users.filter(user =>
        user.nickname.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.id.includes(searchTerm)
    );

    return (
        <div>
            <div className={styles.header}>
                <h2 className={styles.title}>用户管理</h2>
                <div className={styles.searchBox}>
                    <Search size={18} className={styles.searchIcon} />
                    <input
                        type="text"
                        placeholder="搜索昵称/用户ID"
                        className={styles.searchInput}
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </div>
            </div>

            {loading ? (
                <div style={{ textAlign: 'center', padding: '2rem' }}>加载中...</div>
            ) : (
                <div className={styles.tableWrapper}>
                    <table className={styles.table}>
                        <thead>
                            <tr>
                                <th>用户</th>
                                <th>用户ID</th>
                                <th>最后登录</th>
                                <th>状态</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            {filteredUsers.length === 0 ? (
                                <tr>
                                    <td colSpan={5} style={{ textAlign: 'center', padding: '2rem', color: '#64748b' }}>
                                        暂无相关用户
                                    </td>
                                </tr>
                            ) : (
                                filteredUsers.map(user => (
                                    <tr key={user.id}>
                                        <td>
                                            <div className={styles.userInfo}>
                                                <div className={styles.userAvatar}>
                                                    {user.avatarUrl ? (
                                                        <img src={user.avatarUrl} alt="" style={{ width: '100%', height: '100%', borderRadius: '50%' }} />
                                                    ) : (
                                                        user.nickname[0]
                                                    )}
                                                </div>
                                                <span className={styles.userName}>{user.nickname}</span>
                                            </div>
                                        </td>
                                        <td>{user.id}</td>
                                        <td>{user.lastLoginAt}</td>
                                        <td>
                                            <span className={`${styles.statusBadge} ${user.status === 1 ? styles.statusNormal : styles.statusBanned}`}>
                                                {user.status === 1 ? '正常' : '封禁中'}
                                            </span>
                                        </td>
                                        <td>
                                            {user.status === 1 ? (
                                                <button
                                                    className={`${styles.actionBtn} ${styles.banBtn}`}
                                                    onClick={() => toggleStatus(user.id, user.status)}
                                                    title="封禁用户"
                                                >
                                                    <Ban size={14} style={{ marginRight: '4px' }} /> 封禁
                                                </button>
                                            ) : (
                                                <button
                                                    className={styles.actionBtn}
                                                    onClick={() => toggleStatus(user.id, user.status)}
                                                    title="解封用户"
                                                    style={{ color: '#10b981', borderColor: '#10b981' }}
                                                >
                                                    <CheckCircle size={14} style={{ marginRight: '4px' }} /> 解封
                                                </button>
                                            )}
                                        </td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}
