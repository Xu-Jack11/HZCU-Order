import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import OrderCard, { Order } from '../OrderCard';
import '@testing-library/jest-dom';

// Mock data
const mockOrder: Order = {
    id: '1',
    number: 'A001',
    createTime: '12:00',
    status: 'PENDING',
    items: [
        { name: 'Beef Noodle', quantity: 1, spec: 'Spicy' },
        { name: 'Coke', quantity: 2 }
    ],
    totalAmount: 30,
    note: 'No cilantro'
};

const mockOnStatusChange = jest.fn();

describe('OrderCard Component White-box Tests', () => {
    beforeEach(() => {
        mockOnStatusChange.mockClear();
    });

    // 1. Branch Coverage: Status = PENDING
    it('renders correctly for PENDING status', () => {
        render(<OrderCard order={{ ...mockOrder, status: 'PENDING' }} onStatusChange={mockOnStatusChange} />);
        
        expect(screen.getByText('待接单')).toBeInTheDocument();
        expect(screen.getByText('接单')).toBeInTheDocument();
        expect(screen.getByText('拒单')).toBeInTheDocument();
    });

    // 2. Branch Coverage: Status = PROCESSING
    it('renders correctly for PROCESSING status', () => {
        render(<OrderCard order={{ ...mockOrder, status: 'PROCESSING' }} onStatusChange={mockOnStatusChange} />);
        
        expect(screen.getByText('制作中')).toBeInTheDocument();
        expect(screen.getByText('叫号取餐')).toBeInTheDocument();
        expect(screen.queryByText('接单')).not.toBeInTheDocument();
    });

    // 3. Branch Coverage: Status = READY
    it('renders correctly for READY status', () => {
        render(<OrderCard order={{ ...mockOrder, status: 'READY' }} onStatusChange={mockOnStatusChange} />);
        
        expect(screen.getByText('待取餐')).toBeInTheDocument();
        expect(screen.getByText('完成订单')).toBeInTheDocument();
    });

    // 4. Branch Coverage: Status = COMPLETED
    it('renders correctly for COMPLETED status', () => {
        render(<OrderCard order={{ ...mockOrder, status: 'COMPLETED' }} onStatusChange={mockOnStatusChange} />);
        
        expect(screen.getByText('已完成')).toBeInTheDocument();
        expect(screen.getByText('订单已归档')).toBeInTheDocument();
        expect(screen.queryByRole('button')).not.toBeInTheDocument();
    });

    // 5. Branch Coverage: Status = CANCELLED
    it('renders correctly for CANCELLED status', () => {
        render(<OrderCard order={{ ...mockOrder, status: 'CANCELLED' }} onStatusChange={mockOnStatusChange} />);
        
        expect(screen.getByText('已取消')).toBeInTheDocument();
    });

    // 6. Branch Coverage: Optional Note
    it('renders note when provided', () => {
        render(<OrderCard order={{ ...mockOrder, note: 'Extra spicy' }} onStatusChange={mockOnStatusChange} />);
        expect(screen.getByText('备注: Extra spicy')).toBeInTheDocument();
    });

    it('does not render note when undefined', () => {
        const orderWithoutNote = { ...mockOrder };
        delete orderWithoutNote.note;
        render(<OrderCard order={orderWithoutNote} onStatusChange={mockOnStatusChange} />);
        expect(screen.queryByText(/备注:/)).not.toBeInTheDocument();
    });

    // 7. Branch Coverage: Optional Spec
    it('renders item spec when provided', () => {
        render(<OrderCard order={mockOrder} onStatusChange={mockOnStatusChange} />);
        expect(screen.getByText('(Spicy)')).toBeInTheDocument();
    });

    // 8. Interaction/Statement Coverage: Button Clicks
    it('calls onStatusChange with PROCESSING when "接单" is clicked', () => {
        render(<OrderCard order={{ ...mockOrder, status: 'PENDING' }} onStatusChange={mockOnStatusChange} />);
        fireEvent.click(screen.getByText('接单'));
        expect(mockOnStatusChange).toHaveBeenCalledWith('1', 'PROCESSING');
    });

    it('calls onStatusChange with CANCELLED when "拒单" is clicked', () => {
        render(<OrderCard order={{ ...mockOrder, status: 'PENDING' }} onStatusChange={mockOnStatusChange} />);
        fireEvent.click(screen.getByText('拒单'));
        expect(mockOnStatusChange).toHaveBeenCalledWith('1', 'CANCELLED');
    });

    it('calls onStatusChange with READY when "叫号取餐" is clicked', () => {
        render(<OrderCard order={{ ...mockOrder, status: 'PROCESSING' }} onStatusChange={mockOnStatusChange} />);
        fireEvent.click(screen.getByText('叫号取餐'));
        expect(mockOnStatusChange).toHaveBeenCalledWith('1', 'READY');
    });

    it('calls onStatusChange with COMPLETED when "完成订单" is clicked', () => {
        render(<OrderCard order={{ ...mockOrder, status: 'READY' }} onStatusChange={mockOnStatusChange} />);
        fireEvent.click(screen.getByText('完成订单'));
        expect(mockOnStatusChange).toHaveBeenCalledWith('1', 'COMPLETED');
    });
});
