'use client';

import { useState, useRef, useEffect } from 'react';
import { Plus, Edit2, Trash2, X, Search, Upload, ImageIcon } from 'lucide-react';
import styles from './page.module.css';
import { api } from '@/lib/api';

interface Dish {
    id: string;
    name: string;
    price: number;
    category: string;
    categoryId?: number;
    isAvailable: boolean;
    sales: number;
    description?: string;
    image?: string;
}

// Initial fallback categories
const DEFAULT_CATEGORIES = ['全部', '热销', '主食', '套餐', '甜点', '饮品'];

export default function MenuPage() {
    const [dishes, setDishes] = useState<Dish[]>([]);
    const [categories, setCategories] = useState<string[]>(DEFAULT_CATEGORIES);
    const [loading, setLoading] = useState(true);
    const [showModal, setShowModal] = useState(false);
    const [editingDish, setEditingDish] = useState<Dish | null>(null);
    const [searchTerm, setSearchTerm] = useState('');
    const [selectedCategory, setSelectedCategory] = useState('全部');
    const fileInputRef = useRef<HTMLInputElement>(null);

    // Form state
    const [formData, setFormData] = useState({
        name: '',
        price: '',
        category: '套餐',
        description: '',
        image: ''
    });

    const fetchDishes = async () => {
        setLoading(true);
        try {
            const [dishRes, catRes] = await Promise.all([
                api.dishes.getMyDishes(),
                api.dishes.getMyCategories()
            ]);

            if (dishRes.success) {
                const mapped = dishRes.data.map((d: any) => ({
                    id: d.dishId.toString(),
                    name: d.name,
                    price: d.basePrice,
                    category: d.categoryName || '其它',
                    isAvailable: d.status === 1,
                    sales: d.monthSales || 0,
                    description: d.description,
                    image: d.coverImage
                }));
                setDishes(mapped);
            }

            if (catRes.success) {
                const names = catRes.data.map((c: any) => c.name);
                setCategories(['全部', ...names]);
            }
        } catch (err) {
            console.error('Failed to fetch data', err);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchDishes();
    }, []);

    const toggleStatus = async (id: string, currentStatus: boolean) => {
        try {
            const newStatus = currentStatus ? 0 : 1;
            const res = await api.dishes.updateStatus(id, newStatus);
            if (res.success) {
                setDishes(prev => prev.map(d =>
                    d.id === id ? { ...d, isAvailable: !currentStatus } : d
                ));
            }
        } catch (err) {
            alert('操作失败');
        }
    };

    const handleDelete = async (id: string) => {
        if (confirm('确定删除该菜品吗？')) {
            try {
                const res = await api.dishes.deleteDish(id);
                if (res.success) {
                    setDishes(prev => prev.filter(d => d.id !== id));
                }
            } catch (err) {
                alert('删除失败');
            }
        }
    };

    const openAddModal = () => {
        setEditingDish(null);
        // Default to first real category if available
        const defaultCategory = categories.length > 1 ? categories[1] : '其它';
        setFormData({ name: '', price: '', category: defaultCategory, description: '', image: '' });
        setShowModal(true);
    };

    const openEditModal = (dish: Dish) => {
        setEditingDish(dish);
        setFormData({
            name: dish.name,
            price: dish.price.toString(),
            category: dish.category,
            description: dish.description || '',
            image: dish.image || ''
        });
        setShowModal(true);
    };

    const handleImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                setFormData({ ...formData, image: reader.result as string });
            };
            reader.readAsDataURL(file);
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const payload = {
            name: formData.name,
            basePrice: parseFloat(formData.price),
            description: formData.description,
            coverImage: formData.image,
            categoryName: formData.category,
            status: editingDish ? (editingDish.isAvailable ? 1 : 0) : 1
        };

        try {
            if (editingDish) {
                await api.dishes.updateDish(editingDish.id, payload);
            } else {
                await api.dishes.addDish(payload);
            }
            fetchDishes();
            setShowModal(false);
        } catch (err) {
            alert('保存失败');
        }
    };

    const filteredDishes = dishes.filter(dish => {
        const matchesSearch = dish.name.toLowerCase().includes(searchTerm.toLowerCase());
        const matchesCategory = selectedCategory === '全部' || dish.category === selectedCategory;
        return matchesSearch && matchesCategory;
    });

    return (
        <div>
            <div className={styles.header}>
                <h2 className={styles.title}>菜品管理</h2>
                <button className={styles.addButton} onClick={openAddModal}>
                    <Plus size={16} /> 新增菜品
                </button>
            </div>

            <div className={styles.filters}>
                <div className={styles.searchBox}>
                    <Search size={18} className={styles.searchIcon} />
                    <input
                        type="text"
                        placeholder="搜索菜品..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className={styles.searchInput}
                    />
                </div>
                <div className={styles.categoryTabs}>
                    {categories.map(cat => (
                        <button
                            key={cat}
                            className={`${styles.categoryTab} ${selectedCategory === cat ? styles.categoryTabActive : ''}`}
                            onClick={() => setSelectedCategory(cat)}
                        >
                            {cat}
                        </button>
                    ))}
                </div>
            </div>

            {loading ? (
                <div style={{ textAlign: 'center', padding: '3rem' }}>加载中...</div>
            ) : (
                <div className={styles.tableWrapper}>
                    <table className={styles.table}>
                        <thead>
                            <tr>
                                <th style={{ width: '80px' }}>图片</th>
                                <th>菜品名称</th>
                                <th>分类</th>
                                <th>价格</th>
                                <th>销量</th>
                                <th>状态</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            {filteredDishes.length === 0 ? (
                                <tr>
                                    <td colSpan={7} style={{ textAlign: 'center', padding: '3rem', color: '#94a3b8' }}>
                                        暂无符合条件的菜品
                                    </td>
                                </tr>
                            ) : (
                                filteredDishes.map(dish => (
                                    <tr key={dish.id}>
                                        <td>
                                            <div className={styles.dishImage}>
                                                {dish.image ? (
                                                    <img src={dish.image} alt={dish.name} />
                                                ) : (
                                                    <div className={styles.noImage}>
                                                        <ImageIcon size={20} />
                                                    </div>
                                                )}
                                            </div>
                                        </td>
                                        <td>
                                            <div className={styles.dishName}>{dish.name}</div>
                                            {dish.description && <div className={styles.dishDesc}>{dish.description}</div>}
                                        </td>
                                        <td><span className={styles.categoryBadge}>{dish.category}</span></td>
                                        <td className={styles.price}>¥{Number(dish.price).toFixed(2)}</td>
                                        <td>{dish.sales}</td>
                                        <td>
                                            <button
                                                className={`${styles.statusToggle} ${dish.isAvailable ? styles.statusOn : styles.statusOff}`}
                                                onClick={() => toggleStatus(dish.id, dish.isAvailable)}
                                            >
                                                {dish.isAvailable ? '上架中' : '已下架'}
                                            </button>
                                        </td>
                                        <td>
                                            <button className={styles.actionBtn} onClick={() => openEditModal(dish)}>
                                                <Edit2 size={14} /> 编辑
                                            </button>
                                            <button className={`${styles.actionBtn} ${styles.deleteBtn}`} onClick={() => handleDelete(dish.id)}>
                                                <Trash2 size={14} /> 删除
                                            </button>
                                        </td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </table>
                </div>
            )}

            {showModal && (
                <div className={styles.modalOverlay} onClick={() => setShowModal(false)}>
                    <div className={styles.modal} onClick={e => e.stopPropagation()}>
                        <div className={styles.modalHeader}>
                            <h3>{editingDish ? '编辑菜品' : '新增菜品'}</h3>
                            <button className={styles.closeBtn} onClick={() => setShowModal(false)}>
                                <X size={20} />
                            </button>
                        </div>
                        <form onSubmit={handleSubmit} className={styles.form}>
                            <div className={styles.formGroup}>
                                <label>菜品图片</label>
                                <div className={styles.imageUploadArea}>
                                    {formData.image ? (
                                        <div className={styles.imagePreview}>
                                            <img src={formData.image} alt="预览" />
                                            <button
                                                type="button"
                                                className={styles.removeImage}
                                                onClick={() => setFormData({ ...formData, image: '' })}
                                            >
                                                <X size={16} />
                                            </button>
                                        </div>
                                    ) : (
                                        <div
                                            className={styles.uploadPlaceholder}
                                            onClick={() => fileInputRef.current?.click()}
                                        >
                                            <Upload size={32} />
                                            <span>点击上传图片</span>
                                            <span className={styles.uploadHint}>支持 JPG、PNG 格式</span>
                                        </div>
                                    )}
                                    <input
                                        ref={fileInputRef}
                                        type="file"
                                        accept="image/*"
                                        onChange={handleImageUpload}
                                        style={{ display: 'none' }}
                                    />
                                </div>
                            </div>

                            <div className={styles.formGroup}>
                                <label>菜品名称</label>
                                <input
                                    type="text"
                                    value={formData.name}
                                    onChange={e => setFormData({ ...formData, name: e.target.value })}
                                    required
                                    placeholder="请输入菜品名称"
                                />
                            </div>
                            <div className={styles.formRow}>
                                <div className={styles.formGroup}>
                                    <label>价格 (元)</label>
                                    <input
                                        type="number"
                                        step="0.01"
                                        value={formData.price}
                                        onChange={e => setFormData({ ...formData, price: e.target.value })}
                                        required
                                        placeholder="0.00"
                                    />
                                </div>
                                <div className={styles.formGroup}>
                                    <label>分类</label>
                                    <select
                                        value={formData.category}
                                        onChange={e => setFormData({ ...formData, category: e.target.value })}
                                    >
                                        {categories.filter(c => c !== '全部').map(cat => (
                                            <option key={cat} value={cat}>{cat}</option>
                                        ))}
                                    </select>
                                </div>
                            </div>
                            <div className={styles.formGroup}>
                                <label>描述</label>
                                <textarea
                                    value={formData.description}
                                    onChange={e => setFormData({ ...formData, description: e.target.value })}
                                    placeholder="请输入菜品描述（可选）"
                                    rows={3}
                                />
                            </div>
                            <div className={styles.formActions}>
                                <button type="button" className={styles.cancelBtn} onClick={() => setShowModal(false)}>
                                    取消
                                </button>
                                <button type="submit" className={styles.submitBtn}>
                                    {editingDish ? '保存修改' : '添加菜品'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}
