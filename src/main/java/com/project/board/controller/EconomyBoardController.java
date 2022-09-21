package com.project.board.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.project.board.dto.BoardFormDto;
import com.project.board.dto.BoardRequestDto;
import com.project.board.dto.FileDto;
import com.project.board.service.BoardService;
import com.project.board.service.FileService;
import com.project.board.util.MD5Generator;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class EconomyBoardController {
	
	private final BoardService boardService;
	private final FileService fileService;
	

	
	 @GetMapping("/main")
	    public String main(){

	        return "main/main";
	    }
	
	@GetMapping("/board/list")
	public String getBoardListPage(Model model
			, @RequestParam(required = false, defaultValue = "0") Integer page
			, @RequestParam(required = false, defaultValue = "5") Integer size) throws Exception {
		
		try {
			model.addAttribute("resultMap", boardService.findAll(page, size));
		} catch (Exception e) {
			throw new Exception(e.getMessage()); 
		}
		
		return "/board/list";
	}
	
	@GetMapping("/board/write")
	public String getBoardWritePage(Model model, BoardRequestDto boardRequestDto) {
		return "/board/write";
	}
	


	@GetMapping("/board/view")
	public String getBoardViewPage(Model model, BoardRequestDto boardRequestDto) throws Exception {
		
		try {
			if(boardRequestDto.getId() != null) {
				model.addAttribute("info", boardService.findById(boardRequestDto.getId()));
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage()); 
		}
		System.out.println(model);
		
		return "board/view";
	}
	
//	@GetMapping("/board/view/{id}")
//	public String getBoardViewPage(@PathVariable("id") Long id, Model model, BoardRequestDto boardRequestDto) throws Exception {
//		
//	    BoardRequestDto boardrequestDto = boardService.getPost(id);
//        model.addAttribute("post", boardrequestDto);
//		
//		return "board/view";
//	}
	
	@PostMapping("/board/write/action")
	public String boardWriteAction(@RequestParam("file") MultipartFile files,Model model, BoardRequestDto boardRequestDto) 
	{
		
		  try {
	            String origFilename = files.getOriginalFilename();
	            String filename = new MD5Generator(origFilename).toString();
	            /* 실행되는 위치의 'files' 폴더에 파일이 저장됩니다. */
	            String savePath = System.getProperty("user.dir") + "\\files";
	            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
	            if (!new File(savePath).exists()) {
	                try{
	                    new File(savePath).mkdir();
	                }
	                catch(Exception e){
	                    e.getStackTrace();
	                }
	            }
	            String filePath = savePath + "\\" + filename;
	            files.transferTo(new File(filePath));

	            FileDto fileDto = new FileDto();
	            fileDto.setOrigFilename(origFilename);
	            fileDto.setFilename(filename);
	            fileDto.setFilePath(filePath);

	            Long fileId = fileService.saveFile(fileDto);
	            boardRequestDto.setFileId(fileId);
	            
	            boardService.save(boardRequestDto);
	            
	            
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
		return "redirect:/board/list";
	}
	
	@PostMapping("/board/view/action")
	public String boardViewAction(Model model, BoardRequestDto boardRequestDto) throws Exception {
		
		try {
			int result = boardService.updateBoard(boardRequestDto);
			
			if (result < 1) {
				throw new Exception("#Exception boardViewAction!");
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage()); 
		}
		
		return "redirect:/board/list";
	}
	
	@PostMapping("/board/view/delete")
	public String boardViewDeleteAction(Model model, @RequestParam() Long id) throws Exception {
		
		try {
			boardService.deleteById(id);
		} catch (Exception e) {
			throw new Exception(e.getMessage()); 
		}
		
		return "redirect:/board/list";
	}
	
	@PostMapping("/board/delete")
	public String boardDeleteAction(Model model, @RequestParam() Long[] deleteId) throws Exception {
		
		try {
			boardService.deleteAll(deleteId);
		} catch (Exception e) {
			throw new Exception(e.getMessage()); 
		}
		
		return "redirect:/board/list";
	}
	
	@GetMapping("/download/{fileId}")
	public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId) throws IOException {
	    FileDto fileDto = fileService.getFile(fileId);
	    Path path = Paths.get(fileDto.getFilePath());
	    Resource resource = new InputStreamResource(Files.newInputStream(path));
	    return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType("application/octet-stream"))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getOrigFilename() + "\"")
	            .body(resource);
	}
	
	@GetMapping("/board/economy/economyList")
	public String economyForm(Model model) {
		model.addAttribute("boardFormDto", new BoardFormDto());
		return "/board/economy/economyList";
	}
	
	@PostMapping(value = "/board/economy/new")
	public String boardmNew(@Valid BoardFormDto boardFormDto, BindingResult bindingResult,
			Model model, @RequestParam("boardImgFile") List<MultipartFile> boardImgFileList){
		if(bindingResult.hasErrors()){ 
			return "/board/economy/economyWrite";
	}
		if(boardImgFileList.get(0).isEmpty() && boardFormDto.getId() == null){
			model.addAttribute("errorMessage", "첫번째 게시글 이미지는 필수 입력 값 입니다.");
			return "/board/economy/economyWrite";  // 상품 등록시 첫 번째 이미지가 없다면 에러 메시지와 함께 상품등록 페이지로 전환한다.
	} // 상품 첫번째 이미지는 메인 페이지에서 보여줄 상품 이미지를 사용하기 위해 필수 값으로 지정한다.
	
		try {
			boardService.saveBoard(boardFormDto, boardImgFileList); // 상품 저장 로직을 호출. 상품정보와 상품이미지정보를 넘긴다.
		} catch (Exception e){
			model.addAttribute("errorMessage", "게시글 등록 중 에러가 발생하였습니다.");
			return "/board/economy/economyWrite";
		}
		return "/board/economy/economyList"; // 정상적으로 등록되었다면 메인페이지로 이동한다.
	}
	
	@GetMapping(value = "/board/abroad/{boardId}") // url 경로 변수는 { } 표현한다.
	public String boardDtl(@PathVariable("boardId") Long boardId, Model model){
	
		try {
			BoardFormDto boardFormDto = boardService.getBoardDtl(boardId); // 조회한 상품 데이터를 모델에 담아 뷰로 전달한다.
			model.addAttribute("boardFormDto", boardFormDto);
			} catch(EntityNotFoundException e){ // 상품 엔티티가 존재하지 않을 경우 에러 메시지를 담아 상품 등록 페이지로 이동한다.
			model.addAttribute("errorMessage", "존재하지 않는 게시글 입니다.");
			model.addAttribute("boardFormDto", new BoardFormDto());
			return "board/economy/economyWrite";
			}
		return "board/economy/economyWrite";
	}
	
}
