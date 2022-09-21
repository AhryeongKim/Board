package com.project.board.service;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.test.context.support.WithMockUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.project.board.dto.BoardFormDto;
import com.project.board.entity.Board;
import com.project.board.entity.BoardImg;
import com.project.board.repository.BoardImgRepository;
import com.project.board.repository.BoardRepository;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class BoardServiceTest {
	
	@Autowired
	BoardService boardService;
	
	@Autowired
	BoardRepository boardRepository;
	
	@Autowired
	BoardImgRepository boardImgRepository;
	
	List<MultipartFile> createMultipartFiles() throws Exception{
	
		List<MultipartFile> multipartFileList = new ArrayList<>();
		
		for(int i=0;i<5;i++){
			String path = "C:/shop/item/";
			String imageName = "image" + i + ".jpg";
			MockMultipartFile multipartFile =
			new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1,2,3,4});
			multipartFileList.add(multipartFile);
			}
		return multipartFileList;
	}
		

	@Test
	@DisplayName("상품 등록 테스트")
	@WithMockUser(username = "admin", roles = "ADMIN")
	void saveBoard() throws Exception{
		BoardFormDto boardFormDto = new BoardFormDto();
		boardFormDto.setTitle("테스트상품");
		boardFormDto.setContent("테스트 상품 입니다.");

	List<MultipartFile> multipartFileList = createMultipartFiles();
	Long boardId = boardService.saveBoard(boardFormDto, multipartFileList);

	List<BoardImg> boardImgList = boardImgRepository.findByBoardIdOrderByIdAsc(boardId);

	Board board = boardRepository.findById(boardId)
			.orElseThrow(EntityNotFoundException::new);

	assertEquals(boardFormDto.getTitle(), board.getTitle()); // 입력한 상품 데이터와 실제 저장된 상품 데이터가 같은지 확인한다.
	assertEquals(boardFormDto.getContent(), board.getContent());
	assertEquals(multipartFileList.get(0).getOriginalFilename(), boardImgList.get(0).getOriImgName());
}

}
