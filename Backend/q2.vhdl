library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity LAB9_BCD_downCounter is
Port ( clk,rst,load : in STD_LOGIC;
load_inp : in STD_LOGIC_VECTOR (3 downto 0);
q : inout STD_LOGIC_VECTOR (3 downto 0));
end LAB9_BCD_downCounter;

architecture Behavioral of LAB9_BCD_downCounter is
signal div:std_logic_vector(22 downto 0);
signal clkd:std_logic;
begin
process(clkd)
begin
if rising_edge(clk)then
div<= div+1;
end if;
end process;
clkd<=div(22);
process(clkd,rst,load)
begin
if rst='1' then
q<="0000";
elsif rst='0' and load='0' then
if load_inp>=10 or load_inp=0 then
q<="1001";
else
q<=q-1;
end if;
elsif clkd'event and clkd='1' then
q<=q-1;
end if;
end process;
q<=q;
end Behavioral;