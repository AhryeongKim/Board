package com.project.board.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.board.dto.BoardFormDto;
import com.project.board.dto.BoardImgDto;
import com.project.board.dto.BoardRequestDto;
import com.project.board.dto.BoardResponseDto;
import com.project.board.entity.Board;
import com.project.board.entity.BoardImg;
import com.project.board.repository.BoardImgRepository;
import com.project.board.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository boardRepository;
	private final BoardImgService boardImgService;
	private final BoardImgRepository boardImgRepository;
	
	
	@Transactional
	public Long save(BoardRequestDto boardSaveDto) {
		return boardRepository.save(boardSaveDto.toEntity()).getId();
	}
	
	@Transactional(readOnly = true)
	public HashMap<String, Object> findAll(Integer page, Integer size) {
		
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		
		Page<Board> list = boardRepository.findAll(PageRequest.of(page, size));
		
		resultMap.put("list", list.stream().map(BoardResponseDto::new).collect(Collectors.toList()));
		resultMap.put("paging", list.getPageable());
		resultMap.put("totalCnt", list.getTotalElements());
		resultMap.put("totalPage", list.getTotalPages());
		
		return resultMap;
	}
	
	@Transactional
	public BoardRequestDto getPost(Long id) {
	    Board board = boardRepository.findById(id).get();

	    BoardRequestDto boardRequestDto = BoardRequestDto.builder()
	            .id(board.getId())

	            .title(board.getTitle())
	            .content(board.getContent())
	            .fileId(board.getFileId())
	            .registerId(board.getRegisterId())
	            .fileName(board.getFileName())

	            .build();
	    return boardRequestDto;
	}
	
	
	
	
	public BoardResponseDto findById(Long id) {
		return new BoardResponseDto(boardRepository.findById(id).get());
	}
	
	public int updateBoard(BoardRequestDto boardRequestDto) {
		return boardRepository.updateBoard(boardRequestDto);
	}
	
	public int updateBoardReadCntInc(Long id) {
		return boardRepository.updateBoardReadCntInc(id);
	}
	
	public void deleteById(Long id) {
		boardRepository.deleteById(id);
	}
	
	public void deleteAll(Long[] deleteId) {
		boardRepository.deleteBoard(deleteId);
	}
	
	
	public Long saveBoard
	(BoardFormDto boardFormDto, List<MultipartFile> boardImgFileList) throws Exception{
	//상품 등록
	Board board = boardFormDto.createBoard(); // 상품 등록 폼으로부터 입력 받은 데이터를 이용하여 item 객체를 생성한다
	
	boardRepository.save(board); // 상품 데이터를 저장한다
	
	//이미지 등록
	for(int 
	i=0;i<boardImgFileList.size();
	i++){
	BoardImg boardImg = new BoardImg();
	boardImg.setBoard(board);
	
	if(i == 0) //첫 번째 이미지일 경우 대표 상품 이미지 여부 값을 Y로 세팅한다. 나머지 상품 이미지는 N으로 설정한다.
			boardImg.setRepimgYn("Y");
			else
			boardImg.setRepimgYn("N");
			boardImgService.saveBoardImg
			(boardImg, boardImgFileList.get(i)); // 상품 이미지 정보를 저장한다.
			}
			return board.getId();
			}
	
	@Transactional(readOnly = true) // 상품데이터를 읽어오는 트랜잭션 읽기전용 설정한다. 이럴 경우 JPA가 변경감지(더티체킹)를 수행하지 않아서 성능향상할 수 있다.
	public BoardFormDto getBoardDtl(Long boardId){
		List<BoardImg> boardImgList = boardImgRepository.findByBoardIdOrderByIdAsc(boardId); // 해당 상품 이미지 조회
		List<BoardImgDto> boardImgDtoList = new ArrayList<>();
		for (BoardImg boardImg : boardImgList) { // 조회한 ItemImg 엔티티를 ItemImgDto 객체로 만들어서 리스트에 추가한다.
			BoardImgDto boardImgDto = BoardImgDto.of(boardImg);
			boardImgDtoList.add(boardImgDto);
	}
		Board board = boardRepository.findById(boardId) // 상품 아이디를 통해 상품 엔티티를 조회한다. 존재하지 않을 땐 예외를 발생시킨다.
				.orElseThrow(EntityNotFoundException::new);
				BoardFormDto boardFormDto = BoardFormDto.of(board);
				boardFormDto.setBoardImgDtoList(boardImgDtoList);
				return boardFormDto;
				}
	


}
