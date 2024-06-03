import React, { useContext } from 'react'
import "./cart.css"
import { StoreContext } from '../../context/StoreContext'
import { useNavigate } from 'react-router-dom'
const Cart = () => {

    const { cartItems, food_list, removeFromCart, getTotalCartAmount } = useContext(StoreContext)

    const navigate = useNavigate();

    return (
        <div className='cart'>
            <div className="cart-items">
                <div className="cart-items-title">
                    <p>Sản phẩm</p>
                    <p>Tiêu đề</p>
                    <p>Giá</p>
                    <p>Số lượng</p>
                    <p>Tổng tiền</p>
                    <p>Xóa</p>
                </div>
                <br />
                <hr />
                {
                    food_list.map((item, index) => {
                        if (cartItems[item._id] > 0) {
                            return (
                                <div className="">
                                    <div className="cart-items-title cart-items-item">
                                        <img src={item.image} alt="item_image" />
                                        <p>{item.name}</p>
                                        <p>${item.price}</p>
                                        <p>{cartItems[item._id]}</p>
                                        <p>${item.price * cartItems[item._id]}</p>
                                        <p onClick={() => removeFromCart(item._id)} className='cross'>X</p>
                                    </div>
                                    <hr />
                                </div>
                            )
                        }
                    })
                }
            </div>
            <div className="cart-bottom">
                <div className="cart-total">
                    <h2>Chi phí giỏ hàng</h2>
                    <div>
                        <div className="cart-total-details">
                            <p>Giá</p>
                            <p>${getTotalCartAmount()}</p>
                        </div>
                        <hr />
                        <div className="cart-total-details">
                            <p>Phí vận chuyển</p>
                            <p>
                                {
                                    getTotalCartAmount() === 0 ? 0 : 2
                                }
                            </p>
                        </div>
                        <hr />
                        <div className="cart-total-details">
                            <b>Tổng tiền</b>
                            <b>${getTotalCartAmount() === 0 ? 0 : getTotalCartAmount() + 2}</b>
                        </div>
                    </div>
                    <button onClick={() => navigate("/order")}>Tiến hành thanh toán</button>
                </div>
                <div className="cart-promocode">
                    <div className="">
                        <p>Nếu bạn có mã khuyến mãi, Nhập tại đây</p>
                        <div className="cart-promocode-input">
                            <input type="text" placeholder='Mã khuyến mãi' />
                            <button>Xác nhận</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Cart