import React, { useContext, useEffect } from 'react';
import "./cart.css";
import { StoreContext } from '../../context/StoreContext';
import { useNavigate } from 'react-router-dom';
import 'react-loading-skeleton/dist/skeleton.css';
import axios from 'axios';
import NotFound from '../../components/NotFound/NotFound';

const Cart = () => {
    const BASE_URL = "http://localhost:8080";
    const { fetchAllCartByUser, carts, setCarts, profileInfo, removeFromCart } = useContext(StoreContext);



    const getTotalCartAmount = (cartList) => {
        return cartList.reduce((total, item) => total + item.product.price * item.quantity, 0);
    };

    useEffect(() => {
        fetchAllCartByUser();
    }, [profileInfo]);

    const navigate = useNavigate();

    const VNDONG = (number) => {
        return number.toLocaleString('it-IT', { style: 'currency', currency: 'VND' });
    };

    // const removeFromCart = async (id) => {
    //     setCarts(prevCart => prevCart.filter(item => item.product.id !== id));

    //     try {
    //         const response = await axios.delete(`${BASE_URL}/api/v1/carts/${id}`);

    //         if (response.data.statusCode !== 200) {
    //             throw new Error(response.data.message);
    //         }
    //     } catch (e) {
    //         console.log(e.message);
    //         fetchAllCartByUser();
    //     }
    // }

    const handleUpdateQuantity = async (item, quantity) => {

        setCarts(prevCarts =>
            prevCarts.map(cartItem =>
                cartItem.id === item.id ? { ...cartItem, quantity: parseInt(quantity) } : cartItem
            )
        );

        try {
            const response = await axios.put(`${BASE_URL}/api/v1/carts/${item.id}`, {
                ...item,
                quantity: parseInt(quantity)
            });

            if (response.data.statusCode !== 200) {
                throw new Error(response.data.message);
            }
        } catch (e) {
            console.log(e.message);
            fetchAllCartByUser();
        }
    };

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
                    carts
                        ?
                        carts.map((item, index) => (
                            <div key={index}>
                                <div className="cart-items-title cart-items-item">
                                    <img src={item.product.image} alt="item_image" />
                                    <p>{item.product.name}</p>
                                    <p>{VNDONG(item.product.price)}</p>
                                    <input
                                        onChange={(e) => handleUpdateQuantity(item, e.target.value)}
                                        value={item.quantity}
                                        style={{ padding: 6, fontSize: 16, maxWidth: 100 }}
                                        type="number"
                                        min={1}
                                    />
                                    <p>{VNDONG(item.product.price * item.quantity)}</p>
                                    <p onClick={() => removeFromCart(item.id)} className='cross'>X</p>
                                </div>
                                <hr />
                            </div>
                        ))
                        :
                        <NotFound />
                }
            </div>
            <div className="cart-bottom">
                <div className="cart-total">
                    <h2>Chi phí giỏ hàng</h2>
                    <div>
                        <div className="cart-total-details">
                            <p>Giá</p>
                            {
                                carts ?
                                    <p>{VNDONG(getTotalCartAmount(carts))}</p>
                                    :
                                    "0 VND"
                            }
                        </div>
                        <hr />
                        <div className="cart-total-details">
                            <p>Phí vận chuyển</p>
                            <p>Giá</p>
                            {
                                carts ?
                                    <p>{getTotalCartAmount(carts) === 0 ? 0 : VNDONG(30000)}</p>

                                    :
                                    "0 VND"
                            }
                        </div>
                        <hr />
                        <div className="cart-total-details">
                            <b>Tổng tiền</b>
                            <p>Giá</p>
                            {
                                carts ?
                                    <b>{getTotalCartAmount(carts) === 0 ? 0 : VNDONG(getTotalCartAmount(carts) + 30000)}</b>

                                    :
                                    "0 VND"
                            }
                        </div>
                    </div>
                    <button onClick={() => navigate("/order")}>Tiến hành thanh toán</button>
                </div>
                <div className="cart-promocode">
                    <div>
                        <p>Nếu bạn có mã khuyến mãi, Nhập tại đây</p>
                        <div className="cart-promocode-input">
                            <input type="text" placeholder='Mã khuyến mãi' />
                            <button>Xác nhận</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Cart;
