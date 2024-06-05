import React from 'react'
import "./SideBar.css"
import { assets } from '../../admin_assets/assets'
import { NavLink } from 'react-router-dom'

const SideBar = () => {
    return (
        <div className='sidebar'>
            <div className="sidebar-options">
                <NavLink to="/admin/products/add" className="sidebar-option">
                    <img src={assets.add_icon} alt="add_icon" />
                    <p>Thêm sản phẩm</p>
                </NavLink>
                <NavLink to="/admin/products" className="sidebar-option">
                    <img src={assets.order_icon} alt="add_icon" />
                    <p>Sản phẩm</p>
                </NavLink>
                <NavLink to="/admin/orders" className="sidebar-option">
                    <img src={assets.order_icon} alt="add_icon" />
                    <p>Đơn hàng</p>
                </NavLink>
            </div>
        </div>
    )
}

export default SideBar