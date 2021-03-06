package br.com.alura.loja.testes;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.alura.loja.dao.CategoriaDao;
import br.com.alura.loja.dao.ClienteDao;
import br.com.alura.loja.dao.PedidoDao;
import br.com.alura.loja.dao.ProdutoDao;
import br.com.alura.loja.modelo.Categoria;
import br.com.alura.loja.modelo.Cliente;
import br.com.alura.loja.modelo.ItemPedido;
import br.com.alura.loja.modelo.Pedido;
import br.com.alura.loja.modelo.Produto;
import br.com.alura.loja.util.JPAUtil;

public class PerformanceConsultas {

	public static void main(String[] args) {
		popularBancoDeDados();
		EntityManager em = JPAUtil.getEntityManager();

		PedidoDao pedidoDao = new PedidoDao(em);
		Pedido pedido = pedidoDao.buscarPedidoComCliente(1L);

		ProdutoDao produtoDao = new ProdutoDao(em);

		List<Produto> produtos = produtoDao.buscarPorParametros(null, new BigDecimal("800"), null);
//		List<Produto> produtos = produtoDao.buscarPorParametros("PS5", null, null);

		em.close();
		System.out.println(pedido.getCliente().getNome());

		for (Produto produto : produtos) {
			System.out.println(produto.getNome());
			;
		}

	}

	public static void popularBancoDeDados() {
		Categoria celulares = new Categoria("CELULARES");
		Categoria videogames = new Categoria("VIDEOGAMES");
		Categoria informatica = new Categoria("INFORMATICA");

		Produto celular = new Produto("Xiaomi Redmi", "Muito legal", new BigDecimal("800"), celulares);
		Produto videogame = new Produto("PS5", "Playstation 5", new BigDecimal("1500"), videogames);
		Produto macbook = new Produto("Macbook", "Macbook Pro Retina", new BigDecimal("3200"), informatica);

		Cliente cliente = new Cliente("Rodrigo", "80060020000");
		EntityManager em = JPAUtil.getEntityManager();
		ProdutoDao produtoDao = new ProdutoDao(em);
		CategoriaDao categoriaDao = new CategoriaDao(em);
		ClienteDao clienteDao = new ClienteDao(em);

		em.getTransaction().begin();

		categoriaDao.cadastrar(celulares);
		categoriaDao.cadastrar(videogames);
		categoriaDao.cadastrar(informatica);
		produtoDao.cadastrar(celular);
		produtoDao.cadastrar(videogame);
		produtoDao.cadastrar(macbook);
		clienteDao.cadastrar(cliente);

		em.getTransaction().commit();

		Produto produto = produtoDao.buscarPorId(1L);
		Produto produto2 = produtoDao.buscarPorId(2L);
		Produto produto3 = produtoDao.buscarPorId(3L);
		cliente = clienteDao.buscarPorId(1l);

		Pedido pedido = new Pedido(cliente);
		pedido.adicionarItem(new ItemPedido(10, pedido, produto));
		pedido.adicionarItem(new ItemPedido(40, pedido, produto2));

		Pedido pedido2 = new Pedido(cliente);
		pedido.adicionarItem(new ItemPedido(2, pedido, produto3));

		PedidoDao pedidoDao = new PedidoDao(em);

		em.getTransaction().begin();

		pedidoDao.cadastrar(pedido);
		pedidoDao.cadastrar(pedido2);

		em.getTransaction().commit();
		em.close();
	}

}
