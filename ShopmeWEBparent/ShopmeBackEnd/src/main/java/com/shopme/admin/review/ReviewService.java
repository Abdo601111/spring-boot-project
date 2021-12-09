package com.shopme.admin.review;

import com.shopme.admin.product.ProductRepository;
import com.shopme.common.entity.Review;
import com.shopme.common.exception.ReviewNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ReviewService {

    @Autowired
    private ReviewRepository repo;
    @Autowired
    private ProductRepository productRepository;

    public static final int PAGE_NUMPER=4;



    public List<Review> listReview(){

        return (List<Review>) repo.findAll();

    }
 public Page<Review> ListPage(int number, String keyWord){
        Pageable pageable = PageRequest.of(number -1, PAGE_NUMPER);

        if(keyWord != null) {
            return repo.findAll(keyWord,pageable);
        }

        return repo.findAll(pageable);
}


    public Review get(int id) throws ReviewNotFoundException {
        try {
            return repo.findById(id).get();
        }catch (NoSuchElementException ex){
        throw  new ReviewNotFoundException("Could Not Find Any Review With ID" +id);
        }
    }

       public void save(Review review){
        Review reviewInDB= repo.findById(review.getId()).get();
         reviewInDB.setHeadline(review.getHeadline());
         reviewInDB.setComment(review.getComment());
         repo.save(reviewInDB);
         productRepository.updateReviewAverageRating(reviewInDB.getProduct().getId());
     }
    public void delete(int id) throws ReviewNotFoundException {
        if(!repo.existsById(id)){
            throw  new ReviewNotFoundException("Could Not Find Any Review With ID" +id);
       }
        repo.deleteById(id);
    }

}
