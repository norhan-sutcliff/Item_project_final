package itemservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

@WebServlet("/ItemController")
public class ItemController extends HttpServlet {
	
	@Resource(name = "jdbc/item")
	private DataSource dataSource;
    
	private ItemUtilService itemUtilService;

    
	@Override
	public void init() throws ServletException {
		itemUtilService = new ItemUtilService(dataSource);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String finalAction = request.getParameter("action");
		
		if (finalAction == null) {
			finalAction = "LOAD_ITEMS";
		}
		
		switch(finalAction) {
			case "ADD":
				addItem(request, response);
				break;
			case "LOAD_ITEMS":
				loadItems(request, response);
				break;
			case "LOAD_ITEM":
				loadItem(request, response);
				break;
			case "DELETE":
				deleteItem(request, response);
				break;
			case "UPDATE":
				updateItem(request, response);
				break;
			case "SHOW_DETAILS":
				showDetails(request, response);
				break;
			case "ADD_DETAILS":
				addDetails(request, response);
				break;
			case "ADD_ITEM_DETAILS":
			    addItemDetails(request, response);
			    break;
			case "UPDATE_DETAILS":
				updateDetails(request, response);
				break;
			case "UPDATE_ITEM_DETAILS":
				editDetails(request, response);
			    break;
			case "DELETE_DETAILS":
				deleteDetails(request, response);
				break;
			default:
				loadItems(request, response);
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}
	
	void addItem(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			String name = request.getParameter("nameItem");
			double price = Double.parseDouble(request.getParameter("price"));
			int totalNumber = Integer.parseInt(request.getParameter("totalNumber"));
			
			Item item = new Item(name, price, totalNumber);
			itemUtilService.saveItem(item);
			
			loadItems(request, response);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	void loadItems(HttpServletRequest request, HttpServletResponse response) {
			
		try {	
			List<ShowItemDetails> items = itemUtilService.getAllItemsWithDetailsFlag();
			
			request.setAttribute("allItems", items);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/show-items.jsp");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	void loadItem(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			int id = Integer.parseInt(request.getParameter("id"));
		
			Item item = itemUtilService.findItemById(id);
			
			request.setAttribute("existedItem", item);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/show-item.jsp");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void deleteItem(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			int id = Integer.parseInt(request.getParameter("id"));
		
			itemUtilService.deleteItem(id);
			
			loadItems(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void updateItem(HttpServletRequest request, HttpServletResponse response) {
		
		try {
			int id = Integer.parseInt(request.getParameter("id"));
		
			Item item = itemUtilService.findItemById(id);
			
			request.setAttribute("existedItem", item);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/update-item.jsp");
			dispatcher.forward(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void showDetails(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    try {
	        int id = Integer.parseInt(request.getParameter("id"));
	        ShowItemDetails row = itemUtilService.getShowItemDetailsById(id);
	        request.setAttribute("row", row);
	        RequestDispatcher dispatcher = request.getRequestDispatcher("/show-items-details.jsp");
	        dispatcher.forward(request, response);
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.sendRedirect("ItemController?action=LOAD_ITEMS");
	    }
	}


	private void addDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    try {
	        // Get the item ID from the query string
	        String idParam = request.getParameter("id");
	        if (idParam == null || idParam.isEmpty()) {
	            throw new IllegalArgumentException("Missing item ID in request.");
	        }

	        int itemId = Integer.parseInt(idParam);

	        // Pass it to the JSP
	        request.setAttribute("itemId", itemId);

	        // Forward to the Add Item Details page
	        RequestDispatcher dispatcher = request.getRequestDispatcher("/add-item-details.jsp");
	        dispatcher.forward(request, response);

	    } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("errorMessage", "Error opening Add Details: " + e.getMessage());
	        request.getRequestDispatcher("/show-items-details.jsp").forward(request, response);
	    }
	}
	
	private void addItemDetails(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    try {
	        String model = request.getParameter("model");
	        String type = request.getParameter("type");
	        String description = request.getParameter("description");
	        String itemIdStr = request.getParameter("id");

	        if (itemIdStr == null || itemIdStr.isEmpty()) {
	            throw new IllegalArgumentException("Missing item ID in form submission.");
	        }

	        int itemId = Integer.parseInt(itemIdStr);
	        ItemDetails details = new ItemDetails(type, model, description, itemId);

	        itemUtilService.addDetails(details);

	        response.sendRedirect("ItemController?action=SHOW_DETAILS&id=" + itemId);

	    } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("errorMessage", "Failed to add item details: " + e.getMessage());
	        request.getRequestDispatcher("/add-item-details.jsp").forward(request, response);
	    }
	}

	private void updateDetails(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    try {
	    	  String idParam = request.getParameter("id");
		        if (idParam == null || idParam.isEmpty()) {
		            throw new IllegalArgumentException("Missing item ID in request.");
		        }

		        int itemId = Integer.parseInt(idParam);

		        // Pass it to the JSP
		        request.setAttribute("itemId", itemId);

		        // Forward to the Add Item Details page
		        RequestDispatcher dispatcher = request.getRequestDispatcher("/update-item-details.jsp");
		        dispatcher.forward(request, response);

		    } catch (Exception e) {
		        e.printStackTrace();
		        request.setAttribute("errorMessage", "Error opening Add Details: " + e.getMessage());
		        request.getRequestDispatcher("/show-items-details.jsp").forward(request, response);
		    }
	    
	}

	private void editDetails(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		try {
	        String model = request.getParameter("model");
	        String type = request.getParameter("type");
	        String description = request.getParameter("description");
	        String itemIdStr = request.getParameter("id");

	        if (itemIdStr == null || itemIdStr.isEmpty()) {
	            throw new IllegalArgumentException("Missing item ID in form submission.");
	        }

	        int itemId = Integer.parseInt(itemIdStr);
	        ItemDetails details = new ItemDetails(type, model, description, itemId);

	        itemUtilService.updateDetails(details);

	        response.sendRedirect("ItemController?action=SHOW_DETAILS&id=" + itemId);

	    } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("errorMessage", "Failed to update item details: " + e.getMessage());
	        request.getRequestDispatcher("/update-item-details.jsp").forward(request, response);
	    }

	}
	
	private void deleteDetails(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    try {
	        String idParam = request.getParameter("id");
	        if (idParam == null || idParam.isEmpty()) {
	            throw new IllegalArgumentException("Missing item ID.");
	        }

	        int itemId = Integer.parseInt(idParam);

	        itemUtilService.deleteDetailsByItemId(itemId);

	        // Redirect back to the same item's details page
	        response.sendRedirect("ItemController?action=SHOW_DETAILS&id=" + itemId);

	    } catch (Exception e) {
	        e.printStackTrace();
	        request.setAttribute("errorMessage", "Failed to delete item details: " + e.getMessage());
	        request.getRequestDispatcher("/show-items-details.jsp").forward(request, response);
	    }
	}

}