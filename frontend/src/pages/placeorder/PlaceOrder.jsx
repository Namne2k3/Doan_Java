import React, { useContext } from 'react'
import "./placeorder.css"
import { StoreContext } from '../../context/StoreContext'


const PlaceOrder = () => {

    const { getTotalCartAmount } = useContext(StoreContext)

    return (
        <form className='place-order'>
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
            <div className="place-order-right">
                <div className="cart-total">
                    <h2>Chi phí giỏ hàng</h2>
                    <div>
                        <div className="cart-total-details">
                            <p>Giá</p>
                            <p>{getTotalCartAmount()}</p>
                        </div>
                        <hr />
                        <div className="cart-total-details">
                            <p>Phí vận chuyển</p>
                            <p>{2}</p>
                        </div>
                        <hr />
                        <div className="cart-total-details">
                            <b>Tổng tiền</b>
                            <b>{getTotalCartAmount() + 2}</b>
                        </div>
                    </div>
                    <button>Thanh toán</button>
                </div>
            </div>
        </form>
    )
}

export default PlaceOrder