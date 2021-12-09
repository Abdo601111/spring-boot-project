package com.shopme.admin.review;

import com.shopme.common.entity.Review;
import com.shopme.common.exception.ReviewNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService service;


    @GetMapping("/reviews")
    public String listAll(Model model) {



        return listByPage(1,model,null);
    }

    @GetMapping("/reviews/page/{pageNum}")
    public String listByPage(@PathVariable(name ="pageNum")int pageNum, Model model, @Param("keyWord")String keyWord ) {
        Page<Review> pageReview= service.ListPage(pageNum,keyWord);
        List<Review> listuser= pageReview.getContent();
        long startCount = (pageNum -1) * service.PAGE_NUMPER +1;
        long endCount = startCount +service.PAGE_NUMPER -1;
        if(endCount > pageReview.getTotalElements()) {
            endCount = pageReview.getTotalElements();
        }
        model.addAttribute("currentPage",pageNum);
        model.addAttribute("totalPage",pageReview.getTotalPages());
        model.addAttribute("startCount",startCount);
        model.addAttribute("endCount",endCount);

        model.addAttribute("totalItem",pageReview.getTotalElements());
        model.addAttribute("list_review",listuser);
        model.addAttribute("keyWord",keyWord);


        return "reviews/reviews";
    }

    @GetMapping("/reviews/details/{id}")
    public String viewReview(@PathVariable("id") int id , Model model, RedirectAttributes rs) throws ReviewNotFoundException {

        try {
            Review r= service.get(id);
            model.addAttribute("review",r);
            return "reviews/review_detail_modal";
        }catch (ReviewNotFoundException re){
            rs.addFlashAttribute("message",re.getMessage());
            return "redirect:/reviews";

        }
    }

    @GetMapping("/reviews/edit/{id}")
    public String edit(@PathVariable("id") int id , Model model, RedirectAttributes rs) throws ReviewNotFoundException {

        try {
            Review r= service.get(id);
            model.addAttribute("review",r);
            model.addAttribute("pageTitle","Edit Review");
            return "reviews/review_form";
        }catch (ReviewNotFoundException re){
            rs.addFlashAttribute("message",re.getMessage());
            return "redirect:/reviews";

        }
    }

    @PostMapping("/reviews/save")
    public String save(Review review, RedirectAttributes rs) throws ReviewNotFoundException {

        service.save(review);
     rs.addFlashAttribute("message","The Reviews has Save Successfully");
            return "reviews/reviews_form";

    }

    @GetMapping("/reviews/delete/{id}")
    public String delete(@PathVariable("id") int id ,  RedirectAttributes rs) throws ReviewNotFoundException {

        try {
            service.delete(id);
           rs.addFlashAttribute("message","The Reviews Has Been Deleted Successfully");
        }catch (ReviewNotFoundException re){
            rs.addFlashAttribute("message",re.getMessage());
      }
        return "redirect:/reviews";
    }







}
