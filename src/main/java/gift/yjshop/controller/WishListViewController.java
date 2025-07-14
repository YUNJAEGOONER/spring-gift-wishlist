package gift.yjshop.controller;

import gift.dto.wish.WishRequestDto;
import gift.dto.wish.WishResponseDto;
import gift.entity.Member;
import gift.service.WishListService;
import gift.yjshop.infra.YjUser;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/view/my")
@Controller
public class WishListViewController {

    private final WishListService wishListService;

    public WishListViewController(WishListService wishListService){
        this.wishListService = wishListService;
    }

    //WishList에 담긴 상품 목록을 조회
    @GetMapping("/wishlist")
    public String getWishList(
            @YjUser Member member,
            Model model
    ){
        List<WishResponseDto> myWishList = wishListService.getList(member.getMemberId());
        model.addAttribute("wishlist", myWishList);
        return "/yjshop/user/wishlist";
    }

    //위시리스트에 상품을 추가
    @PostMapping("/wishlist")
    public String addToWishList(
            @ModelAttribute @Valid WishRequestDto wishRequestDto,//상품ID, 수량
            BindingResult bindingResult,
            @YjUser Member member
    ){
        if(bindingResult.hasErrors()){
            return "redirect:/view/products/list";
        }
        wishListService.addToWishList(member.getMemberId(), wishRequestDto);
        return "redirect:/view/my/wishlist";
    }

    //위시 리스트에 담긴 상품을 삭제
    @PostMapping("/wishlist/delete/{wishListId}")
    public String removeWishList(
            @PathVariable Long wishListId
    ){
        wishListService.removeFromWishList(wishListId);
        return "redirect:/view/my/wishlist";
    }

    //동일한 상품을 추가하는 경우 (장바구니 내 물품 수량 조절 + )
    @PostMapping("/wishlist/add/{wishListId}")
    public String addItem(
            @PathVariable Long wishListId,
            @YjUser Member member
    ){
        wishListService.changeQuantity(member.getMemberId(), wishListId, 1);
        return "redirect:/view/my/wishlist";
    }


    //동일한 상품을 제거하는 경우 (장바구니 내 물품 수량 조절 -)
    @PostMapping("/wishlist/subtract/{wishListId}")
    public String subtractItem(
            @PathVariable Long wishListId,
            @YjUser Member member
    ){
        wishListService.changeQuantity(member.getMemberId(), wishListId, -1);
        return "redirect:/view/my/wishlist";
    }

    //결제창 이동
    @GetMapping("/payment")
    public String toPay(){
        return "/yjshop/user/pay";
    }

}