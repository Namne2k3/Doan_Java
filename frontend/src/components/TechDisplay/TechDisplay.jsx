import React, { useContext, useEffect } from 'react'
import "./techdisplay.css"
import { StoreContext } from '../../context/StoreContext'
import TechItem from '../TechItem/TechItem';
import NotFound from '../NotFound/NotFound';

const TechDisplay = ({ category }) => {

    const { fetchProductsByCategory, products } = useContext(StoreContext);

    useEffect(() => {

        fetchProductsByCategory(category)

    }, [category])

    return (
        <div className='tech-display' id='tech-display'>
            <h2>Top món đồ công nghệ</h2>
            {
                products.length !== 0
                    ?
                    <div className="tech-display-list">
                        {
                            products.map((item, index) => {
                                // console.log(item);
                                return (
                                    <TechItem item={item} key={index} />
                                )
                            })
                        }
                    </div>
                    :
                    <NotFound />
            }
        </div>
    )
}

export default TechDisplay