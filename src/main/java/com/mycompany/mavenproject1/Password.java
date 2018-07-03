package com.mycompany.mavenproject1;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Author: Ian Gallagher <igallagher@securityinnovation.com>
 *
 * This code utilizes jBCrypt, which you need installed to use. jBCrypt:
 * http://www.mindrot.org/projects/jBCrypt/
 */
public class Password {
    // Define the BCrypt workload to use when generating password hashes. 10-31 is a valid value.

    private static int workload = 10;

    /**
     * This method can be used to generate a string representing an account
     * password suitable for storing in a database. It will be an OpenBSD-style
     * crypt(3) formatted hash string of length=60 The bcrypt workload is
     * specified in the above static variable, a value from 10 to 31. A workload
     * of 12 is a very reasonable safe default as of 2013. This automatically
     * handles secure 128-bit salt generation and storage within the hash.
     *
     * @param password_plaintext The account's plaintext password as provided
     * during account creation, or when changing an account's password.
     * @return String - a string of length 60 that is the bcrypt hashed password
     * in crypt(3) format.
     */
    public static String hashPassword(String password_plaintext) {
        String salt = BCrypt.gensalt(workload);
        String hashed_password = BCrypt.hashpw(password_plaintext, salt);

        return (hashed_password);
    }

    /**
     * Este método pode ser usado para verificar um hash calculado a partir de
     * um texto simples (por exemplo, durante um login pedido) com o de um hash
     * armazenado de um banco de dados. O hash da senha do banco de dados deve
     * ser passado como a segunda variável.
     *
     * @param password_plaintext A senha de texto simples da conta, conforme
     * fornecida durante uma solicitação de login
     * @param stored_hash O hash de senha armazenado da conta, recuperado do
     * banco de dados de autorização
     * @return boolean - true se a senha corresponder à senha do hash
     * armazenado, caso contrário, false
     */
    public static boolean checkPassword(String password_plaintext, String stored_hash) {
        boolean password_verified = false;

        if (null == stored_hash || !stored_hash.startsWith("$2a$")) {
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");
        }

        password_verified = BCrypt.checkpw(password_plaintext, stored_hash);

        return (password_verified);
    }

    /**
     * Um caso de teste simples para o método principal, verifique se um hash de
     * teste pré-gerado verifica com êxito para a senha que representa, e também
     * gerar um novo hash e garantir que o novo hash verifique apenas o mesmo.
     */
    public static void main(String[] args) {
        String test_passwd = "pedro";
        String test_hash = "$2a$10$1VRokw7POEndEokpHMb87.I5uqd9uJQgxdnaV10Zg/wj5vVfNIJzm";

        System.out.println("Testando o hashing e verificação de senha do BCrypt");
        System.out.println("Testar senha:" + test_passwd);
        System.out.println("Teste armazenado em hash:" + test_hash);
        System.out.println("Hashing test password ...");
        System.out.println();

        String computed_hash = hashPassword(test_passwd);
        System.out.println("Testar o cálculo calculado:" + computed_hash);
        System.out.println();
        System.out.println("Verificando se o hash e o hash armazenado correspondem à senha de teste ...");
        System.out.println();

        String compare_test = checkPassword(test_passwd, test_hash)
                ? "Senhas Match" : "As senhas não coincidem";
        String compare_computed = checkPassword(test_passwd, computed_hash)
                ? "Senhas Match" : "As senhas não coincidem";

        System.out.println("Verificar contra hash armazenado:" + compare_test);
        System.out.println("Verify against computed hash:" + compare_computed);

    }
}
