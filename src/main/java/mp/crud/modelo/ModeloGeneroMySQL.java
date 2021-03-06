package mp.crud.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModeloGeneroMySQL implements ModeloGenero {

    private static final String GET_ALL_GENEROS_QUERY = "SELECT * FROM generos";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM generos WHERE id_genero = ?";

    private static final String GET_GENEROS_BY_BOOK_ID = "SELECT generos.* FROM generos "
            + "LEFT JOIN generos_libros ON generos_libros.genero_id = generos.id_genero "
            + "LEFT JOIN libros ON libros.id_libro = generos_libros.libro_id "
            + "WHERE libros.id_libro = ? ";

    private static final String GET_ID_GENEROS_BY_BOOK_ID = "SELECT generos.id_genero FROM generos "
            + "LEFT JOIN generos_libros ON generos_libros.genero_id = generos.id_genero "
            + "LEFT JOIN libros ON libros.id_libro = generos_libros.libro_id "
            + "WHERE libros.id_libro = ? ";
    
    private static final String ADD_GENERO_QUERY = "INSERT INTO generos VALUES  (null, ?)";
    private static final String UPDATE_GENERO_QUERY = "UPDATE generos SET nombre = ? WHERE id_genero = ?";
    private static final String DELETE_GENERO_QUERY = "DELETE FROM generos WHERE id_genero = ?";
    private static final String DELETE_RELATIONS_BY_ID_GENERO = "DELETE FROM generos_libros "
            + "WHERE genero_id = ?";
    private static final String DELETE_RELATION_BY_ID_LIBRO_ID_GENERO = "DELETE FROM generos_libros "
            + "WHERE libro_id = ? AND genero_id = ?";

    @Override
    public List<Genero> getGeneros() {
        List<Genero> generos = new ArrayList<>();
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(GET_ALL_GENEROS_QUERY);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                generos.add(rsToGenero(rs));
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        }

        return generos;
    }

    @Override
    public Genero getGenero(int id) {
        Genero genero;
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(GET_BY_ID_QUERY);) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery();) {
                rs.next();
                genero = rsToGenero(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener genero", ex);
        }

        return genero;
    }

    @Override
    public List<Genero> getGenerosDeLibro(int idLibro) {
        List<Genero> generos = new ArrayList<>();
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(GET_GENEROS_BY_BOOK_ID);) {
            ps.setInt(1, idLibro);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    generos.add(rsToGenero(rs));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener los generos del libro", ex);
        }

        return generos;
    }
    
    
    @Override
    public List<Integer> getGenerosIdDeLibro(int idLibro) {
        List<Integer> generos = new ArrayList<>();
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(GET_ID_GENEROS_BY_BOOK_ID);) {
            ps.setInt(1, idLibro);
            try (ResultSet rs = ps.executeQuery();) {
                while (rs.next()) {
                    int id = rs.getInt(1);
                    generos.add(id);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener los generos del libro", ex);
        }

        return generos;
    }

    @Override
    public int addGenero(Genero genero) {
        int regsAgregados = 0;
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(ADD_GENERO_QUERY);) {
            ps.setString(1, genero.getNombre());
            regsAgregados = ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al agregar genero", ex);
        }
        return regsAgregados;
    }

    @Override
    public int updateGenero(Genero genero) {
        int regsAgregados = 0;
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(UPDATE_GENERO_QUERY);) {
            ps.setString(1, genero.getNombre());
            ps.setInt(2, genero.getId());
            regsAgregados = ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al actualizar genero", ex);
        }
        return regsAgregados;
    }

    @Override
    public int removeGenero(int id) {
        int regsBorrados = 0;
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(DELETE_GENERO_QUERY);) {
            ps.setInt(1, id);
            regsBorrados = ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al eliminar genero", ex);
        }
        return regsBorrados;
    }

    @Override
    public int removeRelationsOfGenero(int idGenero) {
        int regsBorrados = 0;
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(DELETE_RELATIONS_BY_ID_GENERO);) {
            ps.setInt(1, idGenero);
            regsBorrados = ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al eliminar las relaciones del genero con libros", ex);
        }
        return regsBorrados;
    }

    @Override
    public int removeRelationLibroGenero(int idLibro, int idGenero) {
        int regsBorrados = 0;
        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(DELETE_RELATION_BY_ID_LIBRO_ID_GENERO);) {
            ps.setInt(1, idLibro);
            ps.setInt(2, idGenero);
            regsBorrados = ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al eliminar la relacion del genero con libro", ex);
        }
        return regsBorrados;
    }

    private Genero rsToGenero(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String nombre = rs.getString(2);
        return new Genero(id, nombre);
    }


}
