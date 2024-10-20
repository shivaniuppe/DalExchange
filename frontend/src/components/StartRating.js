import React, { useEffect, useState } from 'react';
import StarIcon from '../assets/icons/star-regular.svg';
import FilledStarIcon from '../assets/icons/star-solid.svg';

const StartRating = ({ totalStars }) => {
  const maxStars = 5;
  const [stars, setStarts] = useState([]);

  useEffect(() => {
    const starList = [];
    for (let i = 0; i < maxStars; i++) {
      if (i < totalStars) {
        starList.push(
          <img key={i} src={FilledStarIcon} alt="star" className="h-6 w-6" />
        );
      } else {
        starList.push(
          <img key={i} src={StarIcon} alt="star" className="h-6 w-6" />
        );
      }
    }
    setStarts(starList);
  }, [totalStars]);

    
  return (
    <div className="flex flew-col items-center gap-1 text-xs font-semibold">
      <div className="flex flex-row">{stars.map((star) => star)}</div>
    </div>
  );
}

export default StartRating;