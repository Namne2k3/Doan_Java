import React, { useContext } from 'react'
import "./techitem.css"
import { images } from '../../assets/images'
import { StoreContext } from '../../context/StoreContext';


const TechItem = ({ item: { id, name, price, description, image } }) => {

    const {
        cartItems,
        addToCart,
        removeFromCart
    } = useContext(StoreContext);

    return (
        <div className='tech-item'>
            <div className="tech-item-img-container">
                <img src={image} className='tech-item-image' alt="tech_item_image" />
                {
                    !cartItems[id]
                        ?
                        <img className='add' onClick={() => addToCart(id)} src={images.add_icon_white} alt='add_icon_white' />
                        :
                        <div className="tech-item-counter">
                            <img onClick={() => removeFromCart(id)} src={images.remove_icon_red} alt="remove_icon_red" />
                            <p>{cartItems[id]}</p>
                            <img onClick={() => addToCart(id)} src={images.add_icon_green} alt="add_icon_green" />
                        </div>
                }
            </div>
            <div className="tech-item-info">
                <div className="tech-item-name-rating">
                    <p>{name}</p>
                    {/* <img src={images.rating_starts} alt="" /> */}
                </div>
                <p className="tech-item-desc">{description}</p>
                <p className="tech-item-price">${price}</p>
            </div>
        </div>
    )
}

export default TechItem