@jars = `find . -name "*.jar"` ;
foreach $jar ( @jars )
{
	chomp( $jar ) ;
	print $jar . "\n" ;
	system( 'jarsigner' , ( '-verify' , $jar ) ) ;
}
