import path from 'path'
console.log("## rollup.config.js: ", path.join(__dirname))

import commonjs from "@rollup/plugin-commonjs"

export default [
    {
    plugins: [ commonjs({
	include: "node_modules/**"
    }) ]
    }
];
