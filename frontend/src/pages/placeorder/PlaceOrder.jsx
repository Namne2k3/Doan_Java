import React, { useContext, useEffect, useState } from 'react'
import "./placeorder.css"
import { StoreContext } from '../../context/StoreContext'
import CheckoutListButton from '../../components/CheckoutListButton/CheckoutListButton'

const PlaceOrder = () => {

    const { getTotalCartAmount, carts, fetchAllCartByUser, profileInfo } = useContext(StoreContext)
    const [paymentMethod, setPaymentMethod] = useState("stripe")

    useEffect(() => {
        fetchAllCartByUser()

    }, [profileInfo]);

    const VNDONG = (number) => {
        return number.toLocaleString('it-IT', { style: 'currency', currency: 'VND' });
    };

    return (
        <div className='place-order'>
            {
                profileInfo == null
                &&
                <div className="place-order-left">
                    <div className="title">Thông tin vận chuyển</div>
                    <div className="multi-fields">
                        <input type="text" placeholder='Họ' />
                        <input type="text" placeholder='Tên' />
                    </div>
                    <input type="text" placeholder='Địa chỉ email' />
                    <input type="text" placeholder='Đường' />
                    <div className="multi-fields">
                        <input type="text" placeholder='Thành phố' />
                        <input type="text" placeholder='Tỉnh' />
                    </div>
                    <div className="multi-fields">
                        <input type="text" placeholder='Mã Zip' />
                        <input type="text" placeholder='Nước' />
                    </div>
                    <input type="text" placeholder='Số điện thoại' />
                </div>
            }
            <div className="place-order-right">
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

                            {
                                carts ?
                                    <b>{getTotalCartAmount(carts) === 0 ? 0 : VNDONG(getTotalCartAmount(carts) + 30000)}</b>

                                    :
                                    "0 VND"
                            }
                        </div>
                    </div>
                    <div className='payment_container'>
                        <h2>Chọn phương thức thanh toán</h2>
                        <hr />
                        <div className='payment_selection'>
                            <div className='payment_select'>
                                <label htmlFor="stripe">Visa</label>
                                <input onInput={() => setPaymentMethod("stripe")} id='stripe' type="radio" name='payment_method' />
                            </div>

                            <div className="payment_select">
                                <label htmlFor="momo">Momo</label>
                                <input onInput={() => setPaymentMethod("momo")} type="radio" id='momo' name='payment_method' />
                            </div>
                            <div className="payment_select">
                                <label htmlFor="cod">COD</label>
                                <input onInput={() => setPaymentMethod("cod")} type="radio" id='cod' name='payment_method' />
                            </div>
                        </div>
                    </div>
                    {
                        paymentMethod === "stripe"
                            ?
                            <CheckoutListButton carts={carts} text='Thanh toán quốc tế' />
                            :
                            paymentMethod === "momo"
                                ?
                                <button onClick={() => console.log("momo")}>Thanh toán với Momo</button>
                                :
                                <button>Thanh toán tại nhà</button>
                    }
                </div>
            </div>
        </div>
    )
}

export default PlaceOrder