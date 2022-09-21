package com.project.board.service;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.project.board.entity.BoardImg;
import com.project.board.repository.BoardImgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardImgService {

	@Value("${boardImgLocation}") // applicatin.properties에 등록한 itemImgLocation값을 불러와
	private String boardImgLocation; // 변수 itemImgLocation 에 넣는다.
	private final BoardImgRepository boardImgRepository;
	private final FileUploadService fileUploadService;
	
	public void saveBoardImg(BoardImg boardImg, MultipartFile boardImgFile) throws Exception{
		String oriImgName = boardImgFile.getOriginalFilename();
		String imgName = "";
		String imgUrl = "";
		
		//파일 업로드
		if(!StringUtils.isEmpty(oriImgName)){
		imgName = fileUploadService.uploadFile(boardImgLocation, oriImgName,
		boardImgFile.getBytes()); // 사용자가 상품 이미지를 등록했다면 저장할 경로, 파일 이름, 파일 바이트수를 파라미터로 하는 uploadFile 매소드를 호출한다.
		imgUrl = "/images/board/" + imgName; // 저장한 상품 이미지를 불러올 경로를 설정한다. C:/shop/images/item/
		}
		//상품 이미지 정보 저장
		boardImg.updateBoardImg(oriImgName, imgName, imgUrl); // 업로드했던 상품 이미지 파일의 원래 이름, 실제 로컬에 저장된 상품 이미지 파일의 이름,
		boardImgRepository.save(boardImg); // 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로 등의 상품 이미지 정보를 저장한다
		}	
	
	
	public void updateBoardImg(Long boardImgId, MultipartFile boardImgFile) throws Exception{
		if(!boardImgFile.isEmpty()){ // 상품 이미지를 수정한 경우 상품 이미지를 업데이트한다.
			BoardImg savedBoardImg = boardImgRepository.findById(boardImgId) // 상품 이미지 아이디를 이용하여 기존 저장했던 상품 이미지
		.orElseThrow(EntityNotFoundException::new); 
			
			//기존 이미지 파일 삭제
		if(!StringUtils.isEmpty(savedBoardImg.getImgName())) { // 기존에 등록된 상품 이미지 파일이 있을 경우 해당 파일을 삭제한다.
			fileUploadService.deleteFile(boardImgLocation+"/"+ savedBoardImg.getImgName());
			}
		
		String oriImgName = boardImgFile.getOriginalFilename(); // 업데이트한
		String imgName = fileUploadService.uploadFile(boardImgLocation, oriImgName, boardImgFile.getBytes()); // 상품이미지 파일 업로드
		String imgUrl = "/images/item/" + imgName;
		savedBoardImg.updateBoardImg(oriImgName, imgName, imgUrl);
			}
		}
	
	
	
}
