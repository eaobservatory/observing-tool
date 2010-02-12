$cwd = `pwd` ;
chomp( $cwd ) ;

$local_install_dir = '../install' ;
$local_install_path = "$local_install_dir/classes" ;
$local_jar_dir = "$local_install_dir/lib" ;
@local_tools_dir = () ;
$jar_file = '' ;
$classpath = $local_install_path ;

&main ;

sub main
{
	$gmake = `which gmake` ;
	chomp( $gmake ) ;
	if( system( "$gmake" , ( 'clean' ) ) )
	{
		die "Problem running gmake clean \n" ; 
	}

	$jar_file = 'gemini.jar' ;
	&build( 'GEMINI' ) ;
	$classpath .= ":$cwd/GEMINI/install/lib/gemini.jar" ;
	
	$jar_file = 'orac.jar' ;
	@local_tools_dir = ( '../tools' ) ;
	&build( 'ORAC' ) ;
	$classpath .= ":$cwd/ORAC/install/lib/orac.jar" ;
	
	$jar_file = 'omp.jar' ;
	&build( 'OMP' ) ;
	$classpath .= ":$cwd/OMP/install/lib/omp.jar" ;
	
	$jar_file = 'edfreq.jar' ;
	@local_tools_dir = ( '../../ORAC/tools' ) ;
	&build( 'EDFREQ' ) ;
	$classpath .= ":$cwd/EDFREQ/install/lib/edfreq.jar" ;
	
	$jar_file = 'ot.jar' ;
	@local_tools_dir = ( '../tools' , '../../ORAC/tools' , '../../OMP/tools' ) ;
	&build( 'OT' ) ;

	print "Running make ...\n" ;
	if( system( "$gmake" , ( 'install' ) ) )
	{
		die "Problems running gmake install \n" ;
	}
}

sub build
{
	$package = shift or die "No argument\n" ;
	if( -e "$package/src" )
	{
		chdir "$package/src" or die "Could not cd to $package/src \n" ;

		&all ;

		chdir $cwd or die "Could not cd to $cwd \n" ;
	}
}

sub all
{
	&clean ;
	&mkdir ;
	&javac ;
	&jar ;
}

sub clean
{
	if( -e $local_install_path ){ `rm -rf $local_install_path` ; }
	if( -e $local_jar_dir ){ `rm -rf $local_jar_dir` ; }
	if( -e $local_install_dir ){ `rm -rf $local_install_dir` ; }
	print "Cleaned\n" ;
}

sub mkdir
{
	unless( -e $local_install_dir ){ `mkdir -p $local_install_dir` ; }
	unless( -e $local_install_path ){ `mkdir -p $local_install_path` ; }
	unless( -e $local_jar_dir ){ `mkdir -p $local_jar_dir` ; }
	print "Directories created\n" ;
}

sub javac
{
	$javac = `which javac` ;
	chomp( $javac ) ;

	@source_files = () ;
	@files = `find . -name "*.java"` ;
	foreach $file ( @files )
	{
		chomp( $file ) ;
		$file = substr $file , 2 ;
		push( @source_files , $file ) ;
	}

	$local_classpath = $classpath ;
	foreach $dir ( @local_tools_dir )
	{
		@tools = `find $dir -name "*.jar"` ;
		foreach $tool ( @tools ){ chomp( $tool ) ; $local_classpath .= ":$tool" ; }
	}

	@compile_args = () ;
	push( @compile_args , '-target' , '1.6' ) ;
	push( @compile_args , '-d' , $local_install_path ) ;
	push( @compile_args , '-classpath' , $local_classpath ) ;
	push( @compile_args , '-sourcepath' , '.' ) ;
	push( @compile_args , @source_files ) ;

	unless( system( $javac , @compile_args ) )
	{
		print "Compiled " . @source_files . " files\n" ;
	}
	else
	{
		die "Problems compiling\n" ;
	}

	@images = `find . -name images` ;
	foreach $image_folder ( @images )
	{
		chomp( $image_folder ) ;
		$image_folder = substr $image_folder , 2 ;
		$image_dir = $image_folder ;
		$image_dir =~ s/images// ;
		$image_dir = "$local_install_path/$image_dir" ;
		if( system( 'cp' , ( '-r' , $image_folder , $image_dir ) ) )
		{
			die "Couldn't copy $image_folder to $image_dir" ;
		}
	}
}

sub jar
{
	$jar = `which jar` ;
	chomp( $jar ) ;

	@jar_args = () ;
	push( @jar_args , 'cf' , "$local_jar_dir/$jar_file" ) ;
	push( @jar_args , '-C' , $local_install_path ) ;
	push( @jar_args , '.' ) ;
	unless( system( $jar , @jar_args ) )
	{
		print "Jar'd\n" ;
	}
	else
	{
		die "Problems Jar'ing" ;
	}
}
