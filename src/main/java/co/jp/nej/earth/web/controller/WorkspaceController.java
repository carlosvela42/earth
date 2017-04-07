package co.jp.nej.earth.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co.jp.nej.earth.dto.WorkspaceDto;
import co.jp.nej.earth.exception.EarthException;
import co.jp.nej.earth.model.Message;
import co.jp.nej.earth.model.MgrWorkspace;
import co.jp.nej.earth.model.MgrWorkspaceConnect;
import co.jp.nej.earth.service.WorkspaceService;

/**
 * @author longlt
 *
 */

@Controller
@RequestMapping("/workspace")
public class WorkspaceController {

	@Autowired
	WorkspaceService workspaceService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String displayWorkspace(Model model) throws EarthException {
		List<MgrWorkspace> mgrWorkspace = workspaceService.getAll();
		model.addAttribute("mgrWorkspaces", mgrWorkspace);
		return "workspace/workspaceList";
	}

	@RequestMapping(value = "/addNew", method = RequestMethod.GET)
	public String addNew(Model model) {
		MgrWorkspaceConnect mgrWorkspaceConnect = new MgrWorkspaceConnect();
		model.addAttribute("mgrWorkspaceConnect", mgrWorkspaceConnect);
		return "workspace/addNewWorkspace";
	}

	@RequestMapping(value = "/insertOne", method = RequestMethod.POST)
	public String insertOne(@ModelAttribute("mgrWorkspaceConnect") MgrWorkspaceConnect mgrWorkspaceConnect, Model model)
			throws EarthException {
		String result = "";
		List<Message> messages = workspaceService.validateInsert(mgrWorkspaceConnect);

		if (messages != null && messages.size() > 0) {
			model.addAttribute("messages", messages);
			model.addAttribute("mgrWorkspaceConnect", mgrWorkspaceConnect);
			result = "workspace/addNewWorkspace";
		} else {
			boolean success = workspaceService.insertOne(mgrWorkspaceConnect);
			if (success) {
				result = "redirect:list";
			} else {
				result = "workspace/addNewWorkspace";
			}
		}
		return result;
	}

	@RequestMapping(value = "/updateOne", method = RequestMethod.POST)
	public String updateOne(@ModelAttribute("mgrWorkspaceConnect") MgrWorkspaceConnect mgrWorkspaceConnect, Model model)
			throws EarthException {
		String result = "";
		List<Message> messages = workspaceService.validateUpdate(mgrWorkspaceConnect);

		if (messages != null && messages.size() > 0) {
			model.addAttribute("messages", messages);
			model.addAttribute("mgrWorkspaceConnect", mgrWorkspaceConnect);
			result = "workspace/editWorkspace";
		} else {
			boolean success = workspaceService.updateOne(mgrWorkspaceConnect);
			if (success) {
				result = "redirect:list";
			} else {
				result = "workspace/editWorkspace";
			}
		}
		return result;
	}

	@RequestMapping(value = "/showDetail", method = RequestMethod.GET)
	public String showDetail(String workspaceId, Model model) throws EarthException {
		MgrWorkspaceConnect mgrWorkspaceConnect = workspaceService.getDetail(workspaceId);
		model.addAttribute("mgrWorkspaceConnect", mgrWorkspaceConnect);

		return "workspace/editWorkspace";
	}

	@RequestMapping(value = "/deleteList", method = RequestMethod.POST)
	public String deleteList(@ModelAttribute("workspaceIds") String workspaceIds) {

		List<String> workspaceIdList = Arrays.asList(workspaceIds.split("\\s*,\\s*"));
		try {
			boolean status = workspaceService.deleteList(workspaceIdList);

		} catch (EarthException ex) {
			ex.getMessage();
		}
		return "redirect:list";
	}
}
