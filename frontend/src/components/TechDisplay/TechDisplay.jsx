import React, { useContext } from 'react'
import "./techdisplay.css"
import { StoreContext } from '../../context/StoreContext'
import TechItem from '../TechItem/TechItem';

const TechDisplay = ({ category }) => {

    const { food_list } = useContext(StoreContext);

    return (
        <div className='tech-display' id='tech-display'>
            <h2>Top món đồ công nghệ</h2>
            <div className="tech-display-list">
                {
                    food_list.map((item, index) => {
                        if (category === "All" || category === item.category) {
                            return (
                                <TechItem item={item} key={index} />
                            )
                        }
                    })
                }
            </div>
        </div>
    )
}

export default TechDisplay