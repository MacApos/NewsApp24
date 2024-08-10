//package com.elasticBeanstalk.mapper;
//
//import com.elasticBeanstalk.domain.Image;
//import com.elasticBeanstalk.domain.Value;
//import com.library.dto.Article;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.Named;
//
//@Mapper(componentModel = "spring")
//public interface ArticleMapper {
//    @Mapping(target = "contentUrl", source = "image", qualifiedByName = "imageToContentUrl")
//    Article valueToArticle(Value value);
//
//    @Named("imageToContentUrl")
//    static String imageToContentUrl(Image image) {
//        if(image!=null){
//            return image.getContentUrl();
//        }
//        return null;
//    }
//
//}
